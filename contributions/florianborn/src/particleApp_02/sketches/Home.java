package particleApp_02.sketches;


import java.util.ArrayList;
import mathematik.Vector3f;
import processing.core.PApplet;
import teilchen.BehaviorParticle;
import teilchen.Particle;
import teilchen.Physics;
import teilchen.behavior.Seek;
import teilchen.force.Attractor;
import teilchen.force.ViscousDrag;
import teilchen.force.simplevectorfield.SimpleVectorField;


public class Home extends PApplet {

    private int particleNumber = 10000;

    private int gridSize = 20;

    private float mNoiseScale = 0.024f;

    private float mOffset = 0.0f;

    private Physics mPhysics;

    private SimpleVectorField mField;

    private SimpleVectorField mField2;

    private Attractor attractor1;

    private Seek s;

    private ArrayList<BehaviorParticle> particles;

    private ArrayList<Attractor> attractor;

    private ArrayList<Attractor> deflector;

    private ArrayList<Seek> seeks;

    /*Controls*/
    private Util util;

    private boolean drawForces;

    private boolean addAttractor;

    private boolean addDeflector;

    private float seekScale;

    private boolean addForces;

    public void setup() {
        size(1024, 768, OPENGL);
        background(80);
        strokeWeight(1);
        stroke(255);
        noFill();
        ellipseMode(CENTER);
        mPhysics = new Physics();
        util = new Util(this);

        particles = new ArrayList<BehaviorParticle>();
        attractor = new ArrayList<Attractor>();
        deflector = new ArrayList<Attractor>();
        seeks = new ArrayList<Seek>();
        
        for (int i = 0; i < 20; i++) {
            seeks.add(new Seek(i));
        }
        float step = 0;
        for(Seek e : seeks){
            e.setPositionRef(new Vector3f(width/2 + sin(step)*300, height/2 + cos(step)*300));
            e.scale(100);
            step++;
        }
        
        for (int i = 0; i < particleNumber; i++) {
            particles.add(new BehaviorParticle());
        }

        /*Forces*/
        mField = new SimpleVectorField(width, height, gridSize);
        mField.setForceScale(20.0f);
        mField.setNoiseScale(0.09f);
        mField2 = new SimpleVectorField(width, height, gridSize);
        mField2.setNoiseScale(mNoiseScale);
        mField2.setForceScale(60.0f);
        attractor1 = new Attractor();
        attractor1.setPositionRef(new Vector3f(width / 2, height / 2, 0.0f));
        attractor1.radius(100);
        attractor1.strength(-500);
        

        s = new Seek();
        s.setPositionRef(new Vector3f(width / 2, height / 2, 0.0f));
        s.scale(120.0f);
        for (BehaviorParticle p : particles) {
            p.setPositionRef(new Vector3f(random(0, width), random(0, height), 0));
            p.mass(random(0.4f, 5.0f));
            p.behaviors().add(s);
            p.maximumInnerForce(1000.0f);
        }
        //mPhysics.add(mField);
        mPhysics.add(mField2);
        mPhysics.add(attractor1);
        mPhysics.add(particles);
        mPhysics.add(new ViscousDrag());
    }

    public void draw() {
        background(0);
        update();
        beginShape(POINTS);
        for (Particle p : mPhysics.particles()) {
            vertex(p.position().x, p.position().y);
        }
        endShape();

        if (drawForces) {
            drawAttractor(attractor1);
//        drawVectorField(mField);
            drawVectorField(mField2);
            for (Attractor a : attractor) {
                drawAttractor(a);
            }
            for (Attractor d : deflector) {
                drawAttractor(d);
            }
        }

    }

    public void update() {
        final float mDeltaTime = 1.0f / frameRate;
        for (Particle p : mPhysics.particles()) {
            if (p.position().x < 0) {
                p.position().x = width;
            }
            if (p.position().x > width) {
                p.position().x = 0;
            }
            if (p.position().y < 0) {
                p.position().y = height;
            }
            if (p.position().y > height) {
                p.position().y = 0;
            }

        }

        mOffset += 0.05f * mDeltaTime;
        mField.setOffset(mOffset);
        mField2.setOffset(mOffset);
        mField2.setNoiseScale(mNoiseScale);
        s.scale(seekScale);
        mField.update();
        mField2.update();
        mPhysics.step(mDeltaTime);


    }

    public void addForce() {

        if (addDeflector) {
            deflector.add(new Attractor());
            Attractor d = deflector.get(deflector.size() - 1);
            d.setPositionRef(new Vector3f(mouseX, mouseY));
            d.radius(100);
            d.strength(-400);
            mPhysics.add(d);
            //addDeflector = false;
        }
        if (addAttractor) {
            attractor.add(new Attractor());
            Attractor a = attractor.get(attractor.size() - 1);
            a.setPositionRef(new Vector3f(mouseX, mouseY));
            a.radius(100);
            a.strength(200);
            mPhysics.add(a);
            //addAttractor = false;
        }

    }

    public void drawAttractor(Attractor a) {

        pushStyle();
        fill(255, 30);
        if (a.strength() < 0) {
            stroke(150, 0, 0);
        } else if (a.strength() > 0) {
            stroke(0, 150, 0);
        }

        strokeWeight(1);

        ellipse(a.position().x, a.position().y, a.radius(), a.radius());
        popStyle();
    }

    public void drawVectorField(SimpleVectorField v) {
        pushStyle();
        stroke(255, 0, 0);
        Vector3f[][] vf = v.getField();
        for (int i = 0; i < vf.length; i++) {
            for (int j = 0; j < vf[i].length; j++) {
                Vector3f dir = vf[i][j];
                Vector3f pos = new Vector3f(i * gridSize, j * gridSize);
                //System.out.println(pos);
                pushMatrix();
                translate(pos.x, pos.y);
                line(0, 0, dir.x * 10, dir.y * 10);
                popMatrix();
            }
        }
        popStyle();
    }

    public void keyPressed() {
        if (key == 'h') {
            util.toggleHide();
        }
    }

    public void mouseClicked() {
        if (addForces) {
            addForce();
        }
        if (addAttractor || addDeflector) {
            addForces = true;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Home.class.getName()});
    }
}
