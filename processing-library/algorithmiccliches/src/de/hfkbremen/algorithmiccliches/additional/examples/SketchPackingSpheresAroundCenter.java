package de.hfkbremen.algorithmiccliches.additional.examples;

import java.util.*;
import processing.core.*;
import teilchen.util.Packing.PackingEntity;
import teilchen.util.*;

/**
 * http://en.wikipedia.org/wiki/Circle_packing_theorem
 */
public class SketchPackingSpheresAroundCenter extends PApplet {

    private Vector<PackingEntity> mEntities;

    private final PVector mCenter = new PVector();

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        smooth();
        mEntities = createRandomEntites(50);
        mCenter.set(width / 2, height / 2, 0);
    }

    public void draw() {
        background(255);

        stroke(0);
        noFill();
        for (int i = 0; i < mEntities.size(); i++) {
            PackingEntity mEntity = mEntities.get(i);
            ellipse(mEntity.position().x, mEntity.position().y,
                    mEntity.radius() * 2, mEntity.radius() * 2);

        }

        final int ITERATIONS = 50;
        for (int i = 1; i < ITERATIONS; i++) {
            attachToMouse();
            Packing.pack(mEntities, mCenter, 1.0f / (float) frameRate * 0.0251f);
        }
    }

    private Vector<PackingEntity> createRandomEntites(int pNumberOfShapes) {
        Vector<PackingEntity> mRandomEntities = new Vector<PackingEntity>();
        for (int i = 0; i < pNumberOfShapes; i++) {
            PackingEntity c = new PackingEntity();
            c.position().set(random(width), random(height), 0);
            c.radius(random(pNumberOfShapes) + 10);
            mRandomEntities.add(c);
        }
        return mRandomEntities;
    }

    public void mousePressed() {
        mCenter.set(mouseX, mouseY, 0);
    }

    public void keyPressed() {
        mEntities = createRandomEntites(50);
    }

    private boolean contains(PackingEntity c, PVector pPosition) {
        float d = Util.distance(c.position(), pPosition);
        return d <= c.radius();
    }

    private void attachToMouse() {
        for (PackingEntity c : mEntities) {
            if (contains(c, new PVector(mouseX, mouseY))) {
                c.position().set(mouseX, mouseY, 0);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchPackingSpheresAroundCenter.class.getName()});
    }
}
