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
//				.inputPreProcessor(0, new FeedForwardToRnnPreProcessor())
//				.inputPreProcessor(1, new RnnToFeedForwardPreProcessor())
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

	public void reset() {
//		ComputationGraphConfiguration conf = new NeuralNetConfiguration.Builder()
//				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).graphBuilder()
//				.addInputs("trainFeatures").setOutputs("pokerBet")
//				.addLayer("L1", new GravesLSTM.Builder()
//				        .nIn(input_features)
//				        .nOut(128)
//				        .activation(Activation.SOFTSIGN)
//				        .weightInit(new NormalDistribution(1, 0.1))
//				        .build(), "trainFeatures")
//				.addLayer("pokerBet",
//						new RnnOutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
//								.activation(Activation.SOFTMAX)
//								.weightInit(new NormalDistribution(1, 0.1))
//								.nIn(128)
//								.nOut(output_size)
//								.build(), "L1")
//				.build();
//		ComputationGraph model = new ComputationGraph(conf);
//		model.init();
//		this.model = model;
	}

	public LSTM_Bot getCopy() throws CloneNotSupportedException {
		return  new LSTM_Bot(this.model.clone(), this.input_features, this.output_size, x, xIter);
	}

	public void modelToFile(int i) throws IOException {
		model.save(new File("brain" + i));
	}

	public float[] predict(float[] xi) {
		x[xIter] = xi;
		float[][][] m = new float[100][3][1];
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

	public void train(int numFiles) throws IOException, InterruptedException {
		CSVSequenceRecordReader trainFeatures = new CSVSequenceRecordReader();
		CSVSequenceRecordReader trainLabels = new CSVSequenceRecordReader();
		trainFeatures.initialize( new NumberedFileInputSplit("data_inputs_%d.csv", 0, numFiles-1));
		System.out.println("got in");
		trainLabels.initialize(new NumberedFileInputSplit("data_outputs_%d.csv", 0, numFiles-1));
		System.out.println("got out");
		System.out.println(trainLabels.next());
		System.out.println(trainFeatures.next());
		DataSetIterator ds = new SequenceRecordReaderDataSetIterator(trainFeatures, trainLabels, 1000, 10, false, SequenceRecordReaderDataSetIterator.AlignmentMode.EQUAL_LENGTH);
		model.fit(ds, 10);
		System.out.println("trained");
	}
}
