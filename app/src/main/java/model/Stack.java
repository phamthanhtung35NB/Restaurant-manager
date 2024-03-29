//package model;
////stack menurastaurant
//public class Stack {
//    private int top;
//    private int maxSize;
//    private MenuRestaurant[] stackArray;
//    //get set stackArray
//    public MenuRestaurant[] getStackArray() {
//        return stackArray;
//    }
//
//    public int size(){
//        return top+1;
//    }
//    public Stack(int maxSize) {
//        this.maxSize = maxSize;
//        stackArray = new MenuRestaurant[maxSize];
//        top = -1;
//    }
//
//    public void push(MenuRestaurant menuRestaurant) {
//        stackArray[++top] = menuRestaurant;
//    }
//
//    public MenuRestaurant pop() {
//        return stackArray[top--];
//    }
//
//    public MenuRestaurant peek() {
//        return stackArray[top];
//    }
//
//    public boolean isEmpty() {
//        return (top == -1);
//    }
//
//    public boolean isFull() {
//        return (top == maxSize - 1);
//    }
//}
