package com.example.z.myapplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;
import com.example.z.myapplication.GraphicOverlay;

public class FaceFeatureTracker extends GraphicOverlay.Graphic {

    private Context context;

    private volatile Face mFace;
    private int mFaceId;

    private static final String TAG = "FaceFeatureTracker";
    FaceFeatureTracker(GraphicOverlay overlay, Context context) {
        super(overlay);
        this.context = context;
    }

    public void setID(int id) {
        mFaceId = id;
    }

    protected int getID() {
        return mFaceId;
    }

    void update(Face face) {
        mFace = face;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        double scale = compScale(canvas);
        drawHelper(canvas, scale);
    }

    /* BUTTOM_MOUTH: 0; LEFT_CHEEK: 1; LEFT_EAR_TIP: 2; LEFT_EAR: 3;
       LEFT_EYE: 4; LEFT_MOUTH: 5; NOSE_BASE: 6; RIGHT_CHEEK: 7;
       RIGHT_EAR_TIP: 8; RIGHT_EAR: 9; RIGHT_EYE: 10; RIGHT_MOUTH: 11 */
    private void drawHelper(Canvas canvas, double scale) {
        for (Landmark landmark: mFace.getLandmarks()) {
            int num = landmark.getType();
            String name = "f" + Integer.valueOf(num).toString();
            int resID = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            Drawable pic = context.getResources().getDrawable(resID);

            float x = translateX(landmark.getPosition().x + 20);
            float y = translateY(landmark.getPosition().y + 20);
//            Log.v(TAG, name + ":" + x + "," + y);

            pic.setBounds((int)x , (int)y, (int) (x + pic.getIntrinsicWidth() * 0.3), (int) (y + pic.getIntrinsicHeight() * 0.3));
            pic.draw(canvas);
        }
    }

    private double compScale(Canvas canvas) {
        double vWidth = canvas.getWidth();
        double vHeight = canvas.getHeight();
        double imgWidth = 1600;
        double imgHeight = 1024;

        return Math.min(vWidth / imgWidth, vHeight / imgHeight);
    }
}
