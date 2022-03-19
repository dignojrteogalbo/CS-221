public final class Cat extends Mammal {
    public Cat(String name, int age) {
        super(name, age);
    }

    @Override
    public void Speak() {
        System.out.println("Meow");        
    }
    
    @Override
    public void Feed() {
        System.out.println("Fed " + GetName() + " fish!");
    }

    public void Pounce() {
        System.out.println(GetName() + " pounced on the laser!");
    }
}
