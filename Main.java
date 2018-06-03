package an.myapplication;

import android.util.Log;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.TouchListener;
import com.jme3.input.controls.TouchTrigger;
import com.jme3.input.event.TouchEvent;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.system.JmeSystem;
import com.jme3.texture.Texture;

import static com.jme3.input.KeyInput.KEY_DOWN;
import static com.jme3.input.event.TouchEvent.Type.MOVE;
import static com.jme3.input.event.TouchEvent.Type.TAP;

public class Main extends SimpleApplication implements ActionListener,TouchListener {


    private BulletAppState bulletAppState = new BulletAppState();

    BasicShadowRenderer bsr;

    Material mat;

    Material mat2;

    Material mat3;

    private boolean left = false, right = false, up = false, down = false;

    private Vector3f walkDirection = new Vector3f();

    private CharacterControl player;



    public void simpleInitApp() {

        bulletAppState = new BulletAppState();

        stateManager.attach(bulletAppState);



        inputManager.addMapping("Touch", new TouchTrigger (0));



        inputManager.addListener(this, new String[]{"Touch"});





        initMaterial();

        createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());



        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);

        player = new CharacterControl(capsuleShape, 0.05f);

        player.setJumpSpeed(20);

        player.setFallSpeed(30);

        player.setGravity(30);

        player.setPhysicsLocation(new Vector3f(0, 10, 0));



        bulletAppState.getPhysicsSpace().add(player);

    }



    public static void createPhysicsTestWorld(Node rootNode, AssetManager assetManager, PhysicsSpace space) {

        AmbientLight light = new AmbientLight();

        light.setColor(ColorRGBA.LightGray);

        rootNode.addLight(light);



        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        material.setTexture("ColorMap", assetManager.loadTexture("smartmonkey.png"));



        Box floorBox = new Box(140, 0.25f, 140);

        Geometry floorGeometry = new Geometry("Floor", floorBox);

        floorGeometry.setMaterial(material);

        floorGeometry.setLocalTranslation(0, -5, 0);

// Plane plane = new Plane();
//
// plane.setOriginNormal(new Vector3f(0, 0.25f, 0), Vector3f.UNIT_Y);
//
// floorGeometry.addControl(new RigidBodyControl(new PlaneCollisionShape (plane), 0));

        floorGeometry.addControl(new RigidBodyControl (0));

        rootNode.attachChild(floorGeometry);

        space.add(floorGeometry);



//movable boxes

        for (int i = 0; i < 12; i++) {

            Box box = new Box(0.25f, 0.25f, 0.25f);

            Geometry boxGeometry = new Geometry("Box", box);

            boxGeometry.setMaterial(material);

            boxGeometry.setLocalTranslation(i, 5, -3);

//RigidBodyControl automatically uses box collision shapes when attached to single geometry with box mesh

            boxGeometry.addControl(new RigidBodyControl(2));

            rootNode.attachChild(boxGeometry);

            space.add(boxGeometry);

        }



    }



    public void initMaterial() {

        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        TextureKey key = new TextureKey("drawable/ic_launcher.png");

        key.setGenerateMips(true);

        Texture tex = assetManager.loadTexture(key);

        tex.setWrap(Texture.WrapMode.Repeat);

        mat.setTexture("ColorMap", tex);



        mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        TextureKey key2 = new TextureKey("drawable/ic_launcher.png");

        key2.setGenerateMips(true);

        Texture tex2 = assetManager.loadTexture(key2);

        mat2.setTexture("ColorMap", tex2);



        mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        TextureKey key3 = new TextureKey("drawable/ic_launcher.png");

        key3.setGenerateMips(true);

        Texture tex3 = assetManager.loadTexture(key3);

        tex3.setWrap(Texture.WrapMode.Repeat);

        mat3.setTexture("ColorMap", tex3);

    }



    @Override

    public void onAction(String binding, boolean value, float tpf) {



        Log.e("",""+binding);

    }



    @Override

    public void onTouch(String binding, TouchEvent evt, float tpf) {

        float x;

        float y;

        float pressure;

        switch(evt.getType())

        {

            case MOVE:

                x = evt.getX();

                y = evt.getY();



                pressure = evt.getPressure();

                break;



            case TAP:

                x = evt.getX();

                y = evt.getY();

                break;



            case LONGPRESSED:

// move forward

                up = true;

                break;



            case UP:

                up = false;

                break;



            case FLING:

                break;



            default:



                break;

        }

        Log.e("","Event Type " + evt.getType());

        evt.setConsumed();





    }



    @Override

    public void simpleUpdate(float tpf) {



        Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);

        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);



        walkDirection.set(0, 0, 0);

        if (left) { walkDirection.addLocal(camLeft); }

        if (right) { walkDirection.addLocal(camLeft.negate()); }

        if (up) { walkDirection.addLocal(camDir); }

        if (down) { walkDirection.addLocal(camDir.negate()); }

        player.setWalkDirection(walkDirection);

        cam.setLocation(player.getPhysicsLocation());

    }

}
