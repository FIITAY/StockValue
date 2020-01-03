class ValueNotFoundException extends Exception{
    public ValueNotFoundException(){
        super("the stock value havent been found");
    }
}