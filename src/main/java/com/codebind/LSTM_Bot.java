package com.codebind;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.datavec.api.records.reader.impl.csv.CSVSequenceRecordReader;
import org.datavec.api.split.NumberedFileInputSplit;
import org.deeplearning4j.datasets.datavec.SequenceRecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.distribution.Distribution;
import org.deeplearning4j.nn.conf.distribution.NormalDistribution;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.conf.preprocessor.FeedForwardToRnnPreProcessor;
import org.deeplearning4j.nn.conf.preprocessor.RnnToCnnPreProcessor;
import org.deeplearning4j.nn.conf.preprocessor.RnnToFeedForwardPreProcessor;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.evaluation.classification.ROC;
import org.nd4j.evaluation.classification.ROCMultiClass;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.LabelLastTimeStepPreProcessor;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.learning.config.AdaGrad;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.deeplearning4j.nn.weights.WeightInit;

public class LSTM_Bot {
	MultiLayerNetwork model;
	int input_features, output_size;
	float[][] x = new float[500][];
	int xIter = 0;

	public LSTM_Bot(int input_features, int output_size) {
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.seed(123)
	            .weightInit(WeightInit.XAVIER)
	            .updater(new Adam())
	            .list()
				.layer(0, new LSTM.Builder()
                        .activation(Activation.TANH)
				        .nIn(input_features)
				        .nOut(128)
                        .build())
				.layer(1, new RnnOutputLayer.Builder(LossFunction.MSE)
                        .activation(Activation.RELU)
						.nIn(128)
						.nOut(10)
                        .build())
				.setInputType(InputType.recurrent(input_features, 2))			
				.build();
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		this.model = model;
		this.input_features = input_features;
		this.output_size = output_size;
		for(int i = 0; i < x.length; i++) {
			x[i] = new float[input_features];
		}
	}

	public LSTM_Bot(MultiLayerNetwork model, int input_features, int output_size,  float[][] x, int xIter) {
		this.input_features = input_features;
		this.output_size = output_size;
		this.x = x;
		this.xIter = xIter;
		this.model = model;
	}

	public LSTM_Bot(File brainFile, int input_features, int output_size) throws IOException {
		this.model = MultiLayerNetwork.load(brainFile, true);
		this.input_features = input_features;
		this.output_size = output_size;
		for(int i = 0; i < x.length; i++) {
			x[i] = new float[input_features];
		}
	}

	public void reset() {
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.seed(123)
	            .weightInit(WeightInit.XAVIER)
	            .updater(new Adam())
	            .list()
				.layer(0, new LSTM.Builder()
                        .activation(Activation.TANH)
				        .nIn(input_features)
				        .nOut(128)
                        .build())
				.layer(1, new RnnOutputLayer.Builder(LossFunction.MSE)
                        .activation(Activation.RELU)
						.nIn(128)
						.nOut(10)
                        .build())
				.setInputType(InputType.recurrent(input_features, 2))			
				.build();
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		this.model = model;
	}

	public LSTM_Bot getCopy() throws CloneNotSupportedException {
		return  new LSTM_Bot(this.model.clone(), this.input_features, this.output_size, x, xIter);
	}

	public void modelToFile(int i) throws IOException {
		model.save(new File("brain" + i));
	}

	public float[] predict(float[] xi) {
		x[xIter] = xi;
		float[][][] m = new float[xIter+1][3][1];
		for(int i = 0; i < m.length; i++) {
			m[i][0][0] = x[i][0];
			m[i][1][0] = x[i][1];
			m[i][2][0] = x[i][2];
		}
		INDArray input = Nd4j.create(m);
		INDArray output = model.output(input);
		float[] out = new float[this.output_size];
		for (int i = 0; i < out.length; i++) {
			out[i] = output.get(NDArrayIndex.point(xIter)).getFloat(i);
		}
		xIter++;
		return out;
	}

	public void train(int numFiles, int gen) throws IOException, InterruptedException {
		CSVSequenceRecordReader trainFeatures = new CSVSequenceRecordReader();
		CSVSequenceRecordReader trainLabels = new CSVSequenceRecordReader();
		trainFeatures.initialize( new NumberedFileInputSplit(gen + "\\data_inputs_%d.csv", 0, (numFiles*4/5)-1));
		trainLabels.initialize(new NumberedFileInputSplit(gen + "\\data_outputs_%d.csv", 0, (numFiles*4/5)-1));
		DataSetIterator ds = new SequenceRecordReaderDataSetIterator(trainFeatures, trainLabels, (numFiles*4/5)/10, output_size, false, SequenceRecordReaderDataSetIterator.AlignmentMode.ALIGN_END);
		this.validate((numFiles*4/5), numFiles, gen);
		model.fit(ds, 400);
		this.validate((numFiles*4/5), numFiles, gen);
		System.out.println("trained");
	}
	
	public void validate(int startFiles, int endFiles, int gen) throws IOException, InterruptedException {
		CSVSequenceRecordReader trainFeatures = new CSVSequenceRecordReader();
		CSVSequenceRecordReader trainLabels = new CSVSequenceRecordReader();
		trainFeatures.initialize( new NumberedFileInputSplit(gen + "\\data_inputs_%d.csv", startFiles, endFiles-1));
		trainLabels.initialize(new NumberedFileInputSplit(gen + "\\data_outputs_%d.csv", startFiles, endFiles-1));
		DataSetIterator ds = new SequenceRecordReaderDataSetIterator(trainFeatures, trainLabels, (endFiles-startFiles), output_size, false, SequenceRecordReaderDataSetIterator.AlignmentMode.ALIGN_END);
		Evaluation eval = new Evaluation(output_size);
		while (ds.hasNext()) {
		    DataSet batch = ds.next();
		    INDArray output = model.output(batch.getFeatures());
		    eval.eval(batch.getLabels(), output);
		}
		eval.stats();
		System.out.println("__________________________");
	}
}
