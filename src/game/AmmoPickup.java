package game;

import city.cs.engine.*;

//creates the ammo pickup object that the player can collect
public class AmmoPickup extends StaticBody {

    // creates an ammo pickup
    public AmmoPickup(World world) {
        super(world, new CircleShape(0.7f));
        addImage(new BodyImage("data/BulletBox.png", 3f));
    }
}