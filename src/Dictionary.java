import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Dictionary {
    AVLTree<String> myDictionaryTree;
    public Dictionary(){
        this.myDictionaryTree = new AVLTree<>();
    }
    public Dictionary(String s){
        this.myDictionaryTree = new AVLTree<>(new BTNode<>(s));
    }
    public Dictionary(File f){
        try{
            this.myDictionaryTree = new AVLTree<>(); //nlogn complexity
            Scanner reader = new Scanner(f);
            while(reader.hasNext()){
                String word = reader.nextLine();
                if(!this.findWord(word)){ // to ensure that every word is inserted once
                    this.myDictionaryTree.insertAVL(word);
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File Not Found");
        }

    }
    public void addWord(String s) throws WordAlreadyExistsException{ //logn complexity
        if(this.myDictionaryTree.search(s)){
            throw new WordAlreadyExistsException();
        }
        else
            this.myDictionaryTree.insertAVL(s);
//            System.out.println("Word added successfully");
    }
    public boolean findWord(String s){ //logn complexity
        if(!this.myDictionaryTree.search(s))
            System.out.println("Word not found");
        return this.myDictionaryTree.search(s);
    }
    public void deleteWord(String s) throws WordNotFoundException{ //logn complexity
        if(!this.myDictionaryTree.search(s)){
            throw new WordNotFoundException();
        }
        this.myDictionaryTree.deleteAVL(s);
    }

    public SLL<String> findSimilar(String s){ //O(n*m) complexity
        if(this.myDictionaryTree.isEmpty()){
            throw new RuntimeException("Dictionary is empty");
        }
        SLL<String> allWordsList = fillMyLinkedList(); //The list that has all the words
        SLL<String> similarList = new SLL<>(); //The final list of similar words
        while(!allWordsList.isEmpty()){
            String word = allWordsList.deleteFromHead();
            if(isOneLetterDifferent(s,word) && !word.equals(s)){
                similarList.addToTail(word);
            }
        }
        return similarList;

    }

    private boolean isOneLetterDifferent(String s1,String s2){
        int lengthS1 = s1.length();
        int lengthS2 = s2.length();
        if(Math.abs(lengthS1-lengthS2)>1){
            return false;
        }

        int difference = 0;
        if(lengthS1==lengthS2){
            for(int i =0;i<lengthS1;i++){
                if(s1.charAt(i)!=s2.charAt(i)){
                    difference++;
                    if (difference > 1) {
                        return false;
                    }
                }
            }
        }
        else if(lengthS2<lengthS1){
            int j = 0;
            int i = 0;
            while(i<s2.length()&& difference<2){
                if(s2.charAt(i)!=s1.charAt(j)){
                    difference++;
                    j++;
                }
                else{
                    j++;
                    i++;
                }
            }
            if(difference==0)
                difference++;
        }

        else{
            int j = 0;
            int i = 0;
            while(i<s1.length() && difference<2){
                if(s1.charAt(i)!=s2.charAt(j)){
                    difference++;
                    j++;
                }
                else{
                    j++;
                    i++;
                }
            }
            if(difference==0)
                difference++;
        }
        return difference==1;
    }
    private SLL<String> fillMyLinkedList(){
        //Using BFS to fill my list
        SLL<BTNode<String>> traversalLinkedList = new SLL<>();
        SLL<String> allWordsList = new SLL<>();
        BTNode<String> node = this.myDictionaryTree.root;
        traversalLinkedList.addToTail(node);
        allWordsList.addToTail(node.data);
        while (!traversalLinkedList.isEmpty()){
            node = traversalLinkedList.deleteFromHead();
            if(node.left != null){
                traversalLinkedList.addToTail(node.left);
                allWordsList.addToTail(node.left.data);}
            if(node.right != null){
                traversalLinkedList.addToTail(node.right);
                allWordsList.addToTail(node.right.data);
            }
        }
        return allWordsList;
    }

    public static void main(String[] args) {
        Dictionary myDictionary = new Dictionary();
        myDictionary.addWord("puinter");
        myDictionary.addWord("printer");
        myDictionary.addWord("pointer");
        myDictionary.addWord("punter");
        myDictionary.addWord("guinter");
        SLL<String> similars = myDictionary.findSimilar("puinter");
        System.out.println(similars);

    }
}
