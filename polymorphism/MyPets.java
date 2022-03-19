public class MyPets {
    public static void main(String[] args) {
        Cat cat = new Cat("kitty", 1);
        Dog dog = new Dog("puppy", 2);

        Mammal[] myPets = { cat, dog };

        cat.SetName("whiskers");
        dog.SetName("spotty");

        for (Mammal pet : myPets) {
            pet.Speak();
            pet.Feed();
            pet.TakeToVet();
        }

        cat.Pounce();
        dog.Fetch();
    }
}
