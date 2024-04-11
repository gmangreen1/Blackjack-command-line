import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;


public class BlackJack {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Deck deck = new Deck();        
        String answer = "";
        boolean playAgain = true;
        int bal = 0;
        int max = Integer.MAX_VALUE;
        System.out.println("Hi welcome to BlackJack! Would you like to play(Y/N)?");
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(answer.contains("Y")) {
            System.out.println("How much would you like your starting balance to be?");
            bal = checkValidNumber(reader, max);
            while(playAgain == true&&bal > 0) {
                deck = setDeck();
                deck = shuffle(deck);
                bal = round(deck,reader,bal);
                displayBal(bal);
                if(bal > 0) {
                    System.out.println("Would you like to play again(Y/N)?");
                }else {
                    System.out.println("Thank you for coming!");
                }
                try {
                    answer = reader.readLine();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(answer.contains("N")) {
                    playAgain = false;
                }
            }
        }
        System.out.println("Thank you for coming!");
        
        
        /*
        for(int i = 0; i < deck.cards.size();i++) {
            System.out.println(deck.cards.get(i).name);
        }*/

    }

    public static Deck setDeck(){
        Deck cards = new Deck();
        cards.cards = new ArrayList<>();
        for(int i = 2; i < 11; i++) {
            for(int j =0; j < 4; j++) {
                Card piece = new Card();
                piece.val = i;
                piece.name = Integer.toString(i);
                cards.cards.add(piece);
            }
            
        }
        for(int j =0; j < 4; j++) {
            Card piece = new Card();
            piece.val = 10;
            piece.name = "J";
            cards.cards.add(piece);
        }
        for(int j =0; j < 4; j++) {
            Card piece = new Card();
            piece.val = 10;
            piece.name = "Q";
            cards.cards.add(piece);
        }
        for(int j =0; j < 4; j++) {
            Card piece = new Card();
            piece.val = 10;
            piece.name = "K";
            cards.cards.add(piece);
        }
        for(int j =0; j < 4; j++) {
            Card piece = new Card();
            piece.val = 1;
            piece.name = "A";
            cards.cards.add(piece);
        }
        return cards;
    }
    public static Deck shuffle(Deck cards){
        Collections.shuffle(cards.cards);
        return cards;
    }
    public void gamePlay() {
        //String
        //while()
    }
    public static int round(Deck deck, BufferedReader reader, int bal) {
        String move = "";
        int wager = 0;
        ArrayList<Card> dealerHand = new ArrayList<>();
        int playerVal = 0;
        ArrayList<Card> playerHand = new ArrayList<>();
        ArrayList<Card> playerHand1 = new ArrayList<>();
        int dealerVal = 0;
        boolean validWager = false;
        boolean gameOver =false;
        boolean playerTurn = true;
        boolean bust = false;
        boolean firstTurn = true;
        boolean split = false;
        
        //Deal players cards
        playerHand.add(deck.cards.get(0));
        deck.cards.remove(0);
        playerHand.add(deck.cards.get(0));
        deck.cards.remove(0);
        //Deal dealers cards
        dealerHand.add(deck.cards.get(0));
        deck.cards.remove(0);
        dealerHand.add(deck.cards.get(0));
        deck.cards.remove(0);
        
        System.out.println("How much would you like to bet?" + " Balance: "+bal);
        wager = checkValidNumber(reader,bal);    
        playerHand = getCardTotalAce(playerHand);
        dealerHand = getCardTotalAce(dealerHand);
        displayPlayerHand(playerHand);
        displayDealerHand(dealerHand,playerTurn);
        if(checkBJ(playerHand)) {
            if(!checkBJ(dealerHand)) {
                System.out.println("Black Jack!");
                return (int) (bal + wager*1.5);
            }else {
                System.out.println("Push");
                return bal;
            }
        }else if(checkBJ(dealerHand)) {
            System.out.println("Dealer cards: "+playerHand(dealerHand));
            System.out.println("Dealer Black Jack");
            return bal - wager;
        }
        if(firstTurn && wager*2 <= bal) {
            if(playerHand.get(0).val == playerHand.get(1).val) {
                move = playerSplitOption(reader);
            }else {
                move = playerFirstTurn(reader);
            }
            firstTurn = false;
        }else {
            move = playerTurn(reader);
        }
        if(move.contains("H")) {
            playerHand.add(deck.cards.get(0));
            deck.cards.remove(0);
            playerHand = getCardTotalAce(playerHand);
            displayPlayerHand(playerHand);
            displayDealerHand(dealerHand,playerTurn);
        }else if(move.contains("S")) {
            playerTurn = false;
        }else if(move.contains("D")){
            playerHand.add(deck.cards.get(0));
            wager*=2;
            playerTurn = false;
        }else if(move.contains("2")) {
            playerHand1.add(playerHand.get(1));
            playerHand.remove(1);
            playerHand.add(deck.cards.get(0));
            split = true;
        }else {
            playerTurn =false;
        }
        while(playerTurn) {       
            playerHand = getCardTotalAce(playerHand);
            if(getCardTotal(playerHand) > 21) {
                System.out.println("Player busted!");
                playerTurn = false;
                bust = true;
                return bal - wager;
            }else if(getCardTotal(playerHand) == 21) {
                System.out.println("21! Dealer Turn");
                playerTurn = false;
                break;
            }
            move = playerTurn(reader);
            if(move.contains("H")) {
                playerHand.add(deck.cards.get(0));
                deck.cards.remove(0);
                playerHand = getCardTotalAce(playerHand);
                displayPlayerHand(playerHand);
                displayDealerHand(dealerHand,playerTurn);
            }else /*if(move.contains("S"))*/ {
                playerTurn = false;
            }
        }
        //if(split) {
            
        //}
        while(!playerTurn) {
            dealerHand = getCardTotalAce(dealerHand);
            displayPlayerHand(playerHand);
            displayDealerHand(dealerHand, playerTurn);
            if(getCardTotal(dealerHand) < 17) {
                dealerHand.add(deck.cards.get(0));
                deck.cards.remove(0);
                
            }else {
                playerTurn = true;
            }
        }
        if(getCardTotal(dealerHand) > 21 || getCardTotal(dealerHand) < getCardTotal(playerHand)) {
            System.out.println("Player win!");
            return bal+wager;
        }else if(getCardTotal(dealerHand) == getCardTotal(playerHand)) {
            System.out.println("Push!");
            return bal;
        }else {
            System.out.println("Dealer win!");
            return bal-wager;
        }
        
    }
    public static String playerTurn(BufferedReader reader) {
        System.out.println("Enter 'H' to hit 'S' to stand");
        String answer = "";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }
    public static String playerFirstTurn(BufferedReader reader) {
        System.out.println("Enter 'H' to hit 'S' to stand 'D' to double");
        String answer = "";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }
    public static String playerSplitOption(BufferedReader reader) {
        System.out.println("Enter 'H' to hit 'S' to stand 'D' to double '2' to Split");
        String answer = "";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }
    public static int getCardTotal(ArrayList<Card> hand) {
        int total = 0;
        for(int i = 0; i < hand.size(); i++) {
            total+=hand.get(i).val;
        }
        return total;
    }
    public static ArrayList<Card> getCardTotalAce(ArrayList<Card> hand) {
        int total = getCardTotal(hand);
        Card ace = new Card();
        if(containsAce(hand) !=-1) {
            if(total <= 11 ) {
                ace.val=11;
                ace.name = "A";
                hand.set(containsAce(hand), ace);
            }else if(total >21 && hand.get(containsAce(hand)).val == 11){
                ace.val=1;
                ace.name = "A";
                hand.set(containsAce(hand), ace);
                System.out.print("working");
            }
        }
        return hand;
    }
    public static String playerHand(ArrayList<Card> hand) {
        String cards = "";
        int value = getCardTotal(hand);
        //if(containsAce(hand)!=-1) {
            hand = getCardTotalAce(hand);
            value = getCardTotal(hand);
        //}
        for(int i = 0; i < hand.size(); i++) {
            cards = cards + hand.get(i).name + " ";
        }
        cards = cards + " Value: " + value;
        return cards;
    }
    public static void displayPlayerHand(ArrayList<Card> hand) {
        System.out.println("Player cards: " + playerHand(hand));
    }
    public static String dealerHand(ArrayList<Card> hand, boolean playerTurn) {
        //String cards = "";
        if(playerTurn) {
            return(hand.get(0).name + " Value: "+ hand.get(0).val);
        }else {
            return(playerHand(hand));
        }
    }
    public static void displayDealerHand(ArrayList<Card> hand, boolean playerTurn) {
        System.out.println("Dealer cards: " + dealerHand(hand, playerTurn));
    }
    public static boolean checkBJ(ArrayList<Card> hand) {
        if(hand.get(0).name=="A"&&hand.get(1).val==10) {
            return true;
        }else if(hand.get(1).name=="A"&&hand.get(0).val==10) {
            return true;
        }else {
            return false;
        }
    }
    public static int checkValidNumber(BufferedReader reader, int max) {
        String answer = "";
        int val = 0;
        boolean valid = false;
        boolean valid1 = false;
        while(!valid) {
            valid1 = true;
            try {
                answer = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                val = Integer.parseInt(answer);
            } catch(NumberFormatException e) {
                System.out.println("Invalid number. Try again"); 
                valid1 = false;
            }
            if(val <= max && val > 0 && valid1) {
                valid = true;
            }else if(valid1){
                System.out.println("Wager exceeds balance.Try again");
            }
        }
        return val;
    }
    public static void displayBal(int bal) {
        System.out.println("Balance: "+ bal);
    }
    public static int containsAce(ArrayList<Card> hand) {
        for(int i =0; i < hand.size();i++) {
            if(hand.get(i).name == "A") {
                return i;
            }
        }
        return -1;
    }
    public static void ace(ArrayList<Card> hand) {
        
    }

}


