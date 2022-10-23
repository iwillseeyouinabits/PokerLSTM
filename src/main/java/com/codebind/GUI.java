package com.codebind;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GUI {

	public void printWinningHand(int rank) {
		String pr;
		if (rank == 8) {
			pr = "hithere   _____ __            _       __    __     ________           __  hithere  / ___// /_________  (_)___ _/ /_  / /_   / ____/ /_  _______/ /_ hithere  \\__ \\/ __/ ___/ _ \\/ / __ `/ __ \\/ __/  / /_  / / / / / ___/ __ \\hithere ___/ / /_/ /  /  __/ / /_/ / / / / /_   / __/ / / /_/ (__  ) / / /hithere/____/\\__/_/   \\___/_/\\__, /_/ /_/\\__/  /_/   /_/\\__,_/____/_/ /_/ hithere                     /____/                                        hithere";
			System.out.println(pr.replace("hithere", "\n"));
		} else if (rank == 7) {
			pr = "    ______                    ____  ____   __ __ _           __hithere   / ____/___  __  _______   / __ \\/ __/  / //_/(_)___  ____/ /hithere  / /_  / __ \\/ / / / ___/  / / / / /_   / ,<  / / __ \\/ __  / hithere / __/ / /_/ / /_/ / /     / /_/ / __/  / /| |/ / / / / /_/ /  hithere/_/    \\____/\\__,_/_/      \\____/_/    /_/ |_/_/_/ /_/\\__,_/   hithere                                                               ";
			System.out.println(pr.replace("hithere", "\n"));
		} else if (rank == 6) {
			pr = "    ______      ____   __  __                    hithere   / ____/_  __/ / /  / / / /___  __  __________ hithere  / /_  / / / / / /  / /_/ / __ \\/ / / / ___/ _ \\hithere / __/ / /_/ / / /  / __  / /_/ / /_/ (__  )  __/hithere/_/    \\__,_/_/_/  /_/ /_/\\____/\\__,_/____/\\___/ hithere                                                 ";
			System.out.println(pr.replace("hithere", "\n"));
		} else if (rank == 5) {
			pr = "    ________           __  hithere   / ____/ /_  _______/ /_ hithere  / /_  / / / / / ___/ __ \\hithere / __/ / / /_/ (__  ) / / /hithere/_/   /_/\\__,_/____/_/ /_/ hithere                           ";
			System.out.println(pr.replace("hithere", "\n"));
		} else if (rank == 4) {
			pr = "   _____ __            _       __    __ hithere  / ___// /_________  (_)___ _/ /_  / /_hithere  \\__ \\/ __/ ___/ _ \\/ / __ `/ __ \\/ __/hithere ___/ / /_/ /  /  __/ / /_/ / / / / /_  hithere/____/\\__/_/   \\___/_/\\__, /_/ /_/\\__/  hithere                     /____/             ";
			System.out.println(pr.replace("hithere", "\n"));
		} else if (rank == 3) {
			pr = "  ________                            ____   __ __ _           __hithere /_  __/ /_  ________  ___     ____  / __/  / //_/(_)___  ____/ /hithere  / / / __ \\/ ___/ _ \\/ _ \\   / __ \\/ /_   / ,<  / / __ \\/ __  / hithere / / / / / / /  /  __/  __/  / /_/ / __/  / /| |/ / / / / /_/ /  hithere/_/ /_/ /_/_/   \\___/\\___/   \\____/_/    /_/ |_/_/_/ /_/\\__,_/   hithere                                                                 ";
			System.out.println(pr.replace("hithere", "\n"));
		} else if (rank == 2) {
			pr = "  ______                  ____        _     hithere /_  __/      ______     / __ \\____ _(_)____hithere  / / | | /| / / __ \\   / /_/ / __ `/ / ___/hithere / /  | |/ |/ / /_/ /  / ____/ /_/ / / /    hithere/_/   |__/|__/\\____/  /_/    \\__,_/_/_/     hithere                                            ";
			System.out.println(pr.replace("hithere", "\n"));
		} else if (rank == 1) {
			pr = "    ____        _     hithere   / __ \\____ _(_)____hithere  / /_/ / __ `/ / ___/hithere / ____/ /_/ / / /    hithere/_/    \\__,_/_/_/     hithere                      ";
			System.out.println(pr.replace("hithere", "\n"));
		} else if (rank == 0) {
			pr = "    __  ___       __       ______               __hithere   / / / (_)___ _/ /_     / ____/___ __________/ /hithere  / /_/ / / __ `/ __ \\   / /   / __ `/ ___/ __  / hithere / __  / / /_/ / / / /  / /___/ /_/ / /  / /_/ /  hithere/_/ /_/_/\\__, /_/ /_/   \\____/\\__,_/_/   \\__,_/   hithere        /____/                                    ";
			System.out.println(pr.replace("hithere", "\n"));
		}
	}

	public void printTitle() {
		String title="@@ /$$$$$$$   /$$$$$$  /$$   /$$ /$$$$$$$$ /$$$$$$$        /$$$$$$$   /$$$$$$  /$$$$$$$$@@| $$__  $$ /$$__  $$| $$  /$$/| $$_____/| $$__  $$      | $$__  $$ /$$__  $$|__  $$__/@@| $$  \\ $$| $$  \\ $$| $$ /$$/ | $$      | $$  \\ $$      | $$  \\ $$| $$  \\ $$   | $$   @@| $$$$$$$/| $$  | $$| $$$$$/  | $$$$$   | $$$$$$$/      | $$$$$$$ | $$  | $$   | $$   @@| $$____/ | $$  | $$| $$  $$  | $$__/   | $$__  $$      | $$__  $$| $$  | $$   | $$   @@| $$      | $$  | $$| $$\\  $$ | $$      | $$  \\ $$      | $$  \\ $$| $$  | $$   | $$   @@| $$      |  $$$$$$/| $$ \\  $$| $$$$$$$$| $$  | $$      | $$$$$$$/|  $$$$$$/   | $$   @@|__/       \\______/ |__/  \\__/|________/|__/  |__/      |_______/  \\______/    |__/   @@                                                                                      @@                                                                                      @@                                                                                      @@";
		System.out.println(title.replace("@@","\n"));
	}

	public void  printNude() {
		String[] nude=new String[]{"                                                  hithere                                                  hithere             @@@@@@@@@@@@ @                       hithere           @@@@@@@@@@@@@@@@@                      hithere          @&@@@@@@@@@@@@@@@@#                     hithere           @ @@@@@@@@@@@@@@@@@  %                 hithere            @ &@@@@@@@@@@@@@@@@@/                 hithere               ,@@@@@@@@@@@@@@@@@@                hithere            @@@@ @@@@@@@@@@@@@@@@@                hithere           @@@@@@@  @@@@@@@@@@@@@@ @              hithere           *@@@@   @@@@@@@@@@@@@@@   @            hithere            @@@@   @@@@@@@@@@@@@@@ @              hithere             @@@@  @@@@@@@@@@@@@@@@               hithere              @@@@ @@@@@@@@@@@@@@@@               hithere               @@@@@@@@@@@@@@@@@@@@@              hithere         #*     @@@@@@@@@@@@@@@@@@@@              hithere       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@             hithere     @@@@@@@@@@@@@@@@@@@ @@@@@@@@@@@@@@           hithere      @@@@@@@@@@@@@@@@@@@ &@@@@@@@@@@@@@#         hithere        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@       hithere          ,@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@     hithere             @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@    hithere                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@    hithere                  .@@@@@@@@@@@@@@@@@@@@@@@@@@@    hithere                     (@@@@@@@@@@@@@@@@@@@@@@@     hithere                        @@@@@@@@@@@@@@@@@@        hithere                        (@@@@@@@@@                hithere                        @@@@@@@@@*                hithere                       @@@@@@&,@@                 hithere                   @@@@@@@@,   @@                 hithere                                                  ","                 ,/                               hithere      @@@@@@@@@@@@@@@@@@@@@                       hithere  @@@@@@@@@@(  @@@@@@@@@@@@@   @@@@@@@@@          hithere  @@@@@@  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@          hithere  @@@@@@  @@@@@@@@@@@@@@@@@@@@  @@@@@@@           hithere& ,@@@@@@@@@@@@@@@@@@@@@@@@@  @@@@@@@/            hithere  *@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@              hithere     @@@@@@@@@@@@@@@@@@@@@@@@@@@@@                hithere    .@@@@@@@@@@@@@@@@@@@@@@@@@@@                  hithere    @@@@@@@@@@@@@@@@@@@@@@@@@@                    hithere     .@@@@@@@@@@@@@@@@@@@@@@@@@@                  hithere        @@@@@@@@@@@@@@@@@@@@@@@@@@                hithere       @@  @@@@@@@@@@@@@@@@@@@@@@@.               hithere         @   @@@@@@@@@@@@@@@@@@@(                 hithere             @@ @@@@@@@@@@@@@@@@                  hithere                  @@@@@@@@@@@@@@                  hithere                 @@@@@@@@@@@@@@                   hithere                @@@@@@@@@@@@@@@                   hithere             @@@@@@@@@@@@@@@@@@@@                 hithere            @@@@@@@@@@@@@@@@@@@@@@@@@.            hithere           @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@.        hithere       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@     hithere      @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  hithere    @@@@@@@@@@@@@@@@ @@@@(  @@@@@@@@@@@@@@@@@@@@@@hithere    @@@@@@@@@@@@@@@  @@@           @@@@@@@@@@@@@@@hithere      @@@@@@@@@@@@                        &@@@@@@ hithere      @@@@@@@@@@@                                 hithere      @@@@@@@@@                                   hithere      @@@@@@@                                     ","                                                  hithere                                                  hithere                   @@@@@                          hithere                 @@@@@@@@@                        hithere           ,@@@@@@@@@@@@@@@@@                     hithere       /@@@@@@@@@@@@@@@@@@@@@@             .@@@@  hithere    .@@@@@@@@/ @@@@@@@@@@@@@@@     /@@@@@@@@@@@@% hithere  @@@@@@@@@    ,@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  hithere   @@@@@@@      @@@@@@@@@@@@@@@@@      @@@@@@@.   hithere    @@@@@@@@&    @@@@@@@@@@@@@@     &@@@@@@@@     hithere     @@@@@@@@@@*  @@@@@@@@@@@@    @@@@@@@@@.      hithere       @@@@@@@@@@@/  @@@@@@@@@%@@@@@@@@@@.        hithere         @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@           hithere           *@@@@@@@@@@@@@@@@@@@@@@@@@@            hithere             *@@@@@@@@@@@@@@@@@@@@@@@             hithere              *@@@@@@@@@@@@@@@@@@@@@@@            hithere               @@@@@@@@@@@@@@@@@@@@@@@@#          hithere                @@@@@@@@@@@@@@@@@@@@@@@@          hithere                ,@@@@@@@@@@@@@@@@@@@@@@           hithere                @@@@@@@@@@@@@@@@@@@@@.            hithere               @@@@@@@@@@@@@@@@@@@@               hithere            @@@@@@@@@@@@@@@@@@@@@&                hithere         @@@@@@@@@@@@@@@@@@@@@@@#                 hithere       ,@@@@@@@@@@@@@@@@@@@@@@@/                  hithere      /@@@@@@@@@@@@@@@@@@@@@@@@                   hithere      @@@@@@@@@@@@@@@@@@@@@@@@*                   hithere     @@@@@@@@@@@@@@@@@@@@@@@@@                    hithere     @@@@@@@@@@@@@@@@@@@@@@@@@                    hithere     @@@@@@@@@@@@@@@@@@@@@@@@@@                   hithere    .@@@@@@@@@@@@@@@@@@@@@@@@@@(                  hithere     @@@@@@@@@@@@@@@@@@@@@@@@@@@                  hithere      @@@@@@@@@@@@*@@@@@@@@@@@@@@                 hithere      .@@@@@@@@@@@  .@@@@@@@@@@@@@                hithere       @@@@@@@@@@@    @@@@@@@@@@@@.               hithere        @@@@@@@@@@     @@@@@@@@@@@@               hithere        @@@@@@@@@.      /@@@@@@@@@@@              hithere        &@@@@@@@@         @@@@@@@@@@              hithere        @@@@@@@@           @@@@@@@@@              hithere       .@@@@@@@&           @@@@@@@@               hithere       &@@@@@@@.          @@@@@@@@@               hithere       .@@@@@@@           @@@@@@@@%               hithere        @@@@@@@            @@@@@@@                hithere        ,@@@@@@            /@@@@@@                hithere         @@@@@*             @@@@@@                hithere          @@@@              @@@@@@                hithere          @@@@@            @@@@@@@.               hithere          @@@@@            @@@@@@@@               hithere          @@@@@@           @  @@@@@               hithere          @@@@@@              /@@@@@@@@           hithere          .@@@@@                                  hithere            *@@                                   hithere                                                 "};
		System.out.println(nude[(int) (Math.random()*nude.length)].replace("hithere","\n"));
	}

	public void  printHand(int[][] hand) throws FileNotFoundException {
		ArrayList<String[]> cardLines= new ArrayList<String[]>();
		for(int[] card : hand) {
			Scanner fRank = new Scanner(new File("CardText/" + card[1] + ".txt"));
			Scanner fSuit = new Scanner(new File("CardText/S" + card[0] + ".txt"));
			String[] suit = new String[12];
			String[] rank = new String[12];
			for(int i = 0; i < suit.length; i++) {
				suit[i] = fSuit.nextLine();
				rank[i] = fRank.nextLine();
			}
			cardLines.add(rank);
			cardLines.add(suit);
			fRank.close();
			fSuit.close();
		}
		System.out.println();
		System.out.println();
		for (int i = 0; i < 12; i++) {
			for (int citer = 0; citer < cardLines.size(); citer++) {
				String[] card = cardLines.get(citer);
				System.out.print(card[i].replace("\n", ""));
			}
			System.out.println();
		}
	}
}
