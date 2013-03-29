package de.peterb.algorithmicclichees.sketches;


import de.peterb.algorithmicclichees.Vector2f;
import java.util.ArrayList;
import processing.core.PApplet;


public class Run extends PApplet {

    private ArrayList<Agent> agents;

    private Nest nest;

    private float nestTimer;

    public void setup() {
        size(1200, 720, OPENGL);
        nest = new Nest();
        nest.position().set(random(0, width), random(0, height));
        frameRate(60);
//        noCursor();

        Vector2f position = new Vector2f(320, 240);
        Vector2f velocity = new Vector2f(2, 1);
        agents = new ArrayList<Agent>();


    }

//    public void mousePressed() {
//        for (int i = 0; i < 1000; i++) {
//            Agent a;
//            a = new Agent(new Vector2f(mouseX, mouseY), 5);
//            a.setMaxAcceleration(random(1, 10));
//            agents.add(a);
//        }
//    }
    public void draw() {

        float deltaTime = 1.0f / frameRate;
        background(255);


        if (mousePressed) {
            for (int i = 0; i < 1000; i++) {
                Agent a;
                a = new Agent(new Vector2f(random(0, width), random(0, height)), random(1, 4), nest);
                a.setMaxAcceleration(random(1, 10));
                agents.add(a);
            }
        }
        if (nestTimer < 1) {
            nestTimer += deltaTime;
        } else {
            nestTimer = 0;
            nest.position().set(random(0, height), random(0, width));
        }
        for (Agent a : agents) {
            a.update(this, deltaTime);
            a.display(this);
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Run.class.getName()});
    }
}
