public abstract class Mammal {
    private String name;
    private int age;

    public Mammal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public abstract void Speak();

    public abstract void Feed();

    public void TakeToVet() {
        System.out.println("Brought " + name + " to the vet!");
    }

    public final String GetName() {
        return name;
    }

    public final int GetAge() {
        return age;
    }

    public final void SetName(String name) {
        this.name = name;
    }

    public final void SetAge(int age) {
        this.age = age;
    }
}
