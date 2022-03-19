public final class Dog extends Mammal {
    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public void Speak() {
        System.out.println("Woof");
    }

    @Override
    public void Feed() {
        System.out.println("Fed " + GetName() + " chicken!");
    }

    public void Fetch() {
        System.out.println(GetName() + " fetched the ball!");
    }
}
