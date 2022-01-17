package com.czl.base.widget;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PieChartView extends View {
    private Paint mPaint;
    private Path mPath;
    private Path drawLinePath = new Path();
    private PathMeasure mPathMeasure = new PathMeasure();
    private Canvas mCanvas;
    private int width;
    private int height;
    private RectF pieRectF;
    private RectF tempRectF;
    private int radius;
    private List<PieChartView.ItemType> itemTypeList;
    private List<PieChartView.ItemType> leftTypeList;
    private List<PieChartView.ItemType> rightTypeList;
    private List<Point> itemPoints;
    private int cell = 0;
    private float innerRadius = 0.0F;
    private float offRadius = 0.0F;
    private float offLine;
    private int textAlpha;
    private Point firstPoint;
    private int backGroundColor = -1;
    private int itemTextSize = 30;
    private int textPadding = 8;
    private int defaultStartAngle = -90;
    private float pieCell;
    private ValueAnimator animator;
    private long animDuration = 1000L;
    private Point startPoint = new Point();
    private Point centerPoint = new Point();
    private Point endPoint = new Point();
    private Point tempPoint = new Point();

    public PieChartView(Context context) {
        super(context);
        this.init();
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    private void init() {
        this.mPaint = new Paint(5);
        this.mPath = new Path();
        this.pieRectF = new RectF();
        this.tempRectF = new RectF();
        this.itemTypeList = new ArrayList();
        this.leftTypeList = new ArrayList();
        this.rightTypeList = new ArrayList();
        this.itemPoints = new ArrayList();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.startAnim();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.animator != null) {
            this.animator.cancel();
        }

    }

    private void startAnim() {
        this.animator = ValueAnimator.ofFloat(new float[]{0.0F, 720.0F});
        this.animator.setDuration(this.animDuration);
        this.animator.setInterpolator(new LinearInterpolator());
        this.animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                if (value < 360.0F) {
                    PieChartView.this.offRadius = value;
                    PieChartView.this.offLine = 0.0F;
                    PieChartView.this.textAlpha = 0;
                } else if (value >= 360.0F) {
                    PieChartView.this.offRadius = 360.0F;
                    PieChartView.this.offLine = (value - 360.0F) / 360.0F;
                    if (PieChartView.this.offLine > 0.5F) {
                        PieChartView.this.textAlpha = (int) (255.0F * ((PieChartView.this.offLine - 0.5F) / 0.5F));
                    } else {
                        PieChartView.this.textAlpha = 0;
                    }
                } else if (value == 720.0F) {
                    PieChartView.this.offRadius = 360.0F;
                    PieChartView.this.offLine = 1.0F;
                    PieChartView.this.textAlpha = 255;
                }

                PieChartView.this.postInvalidate();
            }
        });
        this.animator.start();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        this.radius = Math.min(this.width, this.height) / 4;
        this.pieRectF.set((float) (this.width / 2 - this.radius), (float) (this.height / 2 - this.radius), (float) (this.width / 2 + this.radius), (float) (this.height / 2 + this.radius));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        try {
            this.mCanvas = canvas;
            this.drawPie();
            if (this.offRadius == 360.0F) {
                this.drawTitle();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private void drawTitle() {
        this.resetPaint();
        float startRadius = (float) this.defaultStartAngle;
        int count = this.rightTypeList.size();
        int h;
        if (count > 1) {
            h = this.radius * 2 / (count - 1);
        } else {
            h = this.radius;
        }

        int i;
        PieChartView.ItemType itemType;
        double angle;
        int x;
        int y;
        for (i = 0; i < count; ++i) {
            this.mPath.reset();
            itemType = (PieChartView.ItemType) this.rightTypeList.get(i);
//            if(itemType.isDrawText) {
            angle = 6.283185307179586D * ((double) (startRadius + itemType.radius / 2.0F) / 360.0D);
            x = (int) ((double) (this.width / 2) + (double) this.radius * Math.cos(angle));
            y = (int) ((double) (this.height / 2) + (double) this.radius * Math.sin(angle));
            this.startPoint.set(x, y);
            this.centerPoint.set((int) ((float) (this.width / 2) + (float) this.radius * 1.2F), this.height / 2 - this.radius + h * i);
            this.endPoint.set((int) ((float) this.width * 0.98F), this.centerPoint.y);
            this.mPath.moveTo((float) this.startPoint.x, (float) this.startPoint.y);
            this.mPath.lineTo((float) this.centerPoint.x, (float) this.centerPoint.y);
            this.mPath.lineTo((float) this.endPoint.x, (float) this.endPoint.y);
            this.resetPaint();
            this.mPaint.setStrokeWidth(2.0F);
            this.mPaint.setColor(itemType.color);
            this.mPaint.setStyle(Style.STROKE);
            this.mPathMeasure.setPath(this.mPath, false);
            this.drawLinePath.reset();
            this.mPathMeasure.getSegment(0.0F, this.mPathMeasure.getLength() * this.offLine, this.drawLinePath, true);
            this.mCanvas.drawPath(this.drawLinePath, this.mPaint);
            startRadius += itemType.radius;

            if (this.textAlpha > 0) {
                this.mPaint.setTextSize((float) this.itemTextSize);
                this.mPaint.setStyle(Style.FILL);
                this.mPaint.setTextAlign(Align.CENTER);
                this.mPaint.setAlpha(this.textAlpha);
//                LogUtils.e(itemType.type, itemType.getPercent());
                this.mCanvas.drawText(itemType.type, (float) (this.centerPoint.x + (this.endPoint.x - this.centerPoint.x) / 2+10), (float) (this.centerPoint.y - this.textPadding), this.mPaint);
                this.mPaint.setTextSize((float) (this.itemTextSize * 4 / 5));
                this.mCanvas.drawText(itemType.getPercent(), (float) (this.centerPoint.x + (this.endPoint.x - this.centerPoint.x) / 2+10), (float) (this.centerPoint.y + (this.itemTextSize + this.textPadding) * 4 / 5), this.mPaint);
            }
//            }
        }

        count = this.leftTypeList.size();
        if (count > 1) {
            h = this.radius * 2 / (count - 1);
        } else {
            h = this.radius;
        }

        for (i = 0; i < count; ++i) {
            this.mPath.reset();
            itemType = (PieChartView.ItemType) this.leftTypeList.get(i);
//            if(itemType.isDrawText) {
            angle = 6.283185307179586D * ((double) (startRadius + itemType.radius / 2.0F) / 360.0D);
            x = (int) ((double) (this.width / 2) + (double) this.radius * Math.cos(angle));
            y = (int) ((double) (this.height / 2) + (double) this.radius * Math.sin(angle));
            this.startPoint.set(x, y);
            this.centerPoint.set((int) ((float) (this.width / 2) - (float) this.radius * 1.2F), this.height / 2 - this.radius + h * (count - 1 - i));
            this.endPoint.set((int) ((float) this.width * 0.02F), this.centerPoint.y);
            this.mPath.moveTo((float) this.startPoint.x, (float) this.startPoint.y);
            this.mPath.lineTo((float) this.centerPoint.x, (float) this.centerPoint.y);
            this.mPath.lineTo((float) this.endPoint.x, (float) this.endPoint.y);
            this.resetPaint();
            this.mPaint.setStrokeWidth(2.0F);
            this.mPaint.setColor(itemType.color);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setDither(true);
            this.mPaint.setStyle(Style.STROKE);
            this.mPathMeasure.setPath(this.mPath, false);
            this.drawLinePath.reset();
            this.mPathMeasure.getSegment(0.0F, this.mPathMeasure.getLength() * this.offLine, this.drawLinePath, true);
            this.mCanvas.drawPath(this.drawLinePath, this.mPaint);
            startRadius += itemType.radius;
            if (this.textAlpha > 0) {
                this.mPaint.setTextSize((float) this.itemTextSize);
                this.mPaint.setStyle(Style.FILL);
                this.mPaint.setTextAlign(Align.CENTER);
                this.mPaint.setAlpha(this.textAlpha);
//                LogUtils.e(itemType.type, itemType.getPercent());
                this.mCanvas.drawText(itemType.type, (float) (this.centerPoint.x + (this.endPoint.x - this.centerPoint.x) / 2+10), (float) (this.centerPoint.y - this.textPadding), this.mPaint);
                this.mPaint.setTextSize((float) (this.itemTextSize * 4 / 5));
                this.mCanvas.drawText(itemType.getPercent(), (float) (this.centerPoint.x + (this.endPoint.x - this.centerPoint.x) / 2+10), (float) (this.centerPoint.y + (this.itemTextSize + this.textPadding) * 4 / 5), this.mPaint);
            }
//            }
        }

        if ((float) this.textAlpha == 1.0F) {
            this.itemTypeList.clear();
            this.leftTypeList.clear();
            this.rightTypeList.clear();
            this.itemPoints.clear();
        }

    }

    private void drawPie() {
        if (this.mCanvas != null) {
            this.mCanvas.drawColor(this.backGroundColor);
            this.mPaint.setStyle(Style.FILL);
            int sum = 0;

            ItemType itemType1;
            for (Iterator var2 = this.itemTypeList.iterator(); var2.hasNext(); sum += itemType1.widget) {
                itemType1 = (ItemType) var2.next();
            }

            float a = 360.0F / (float) sum;
            float startRadius = (float) this.defaultStartAngle;
            float sumRadius = 0.0F;
            this.leftTypeList.clear();
            this.rightTypeList.clear();
            this.itemPoints.clear();
            Iterator var5 = this.itemTypeList.iterator();

            while (var5.hasNext()) {
                ItemType itemType = (ItemType) var5.next();
                itemType.radius = (float) itemType.widget * a;
                double al = 6.283185307179586D * ((double) (startRadius + 90.0F) / 360.0D);
                this.tempPoint.set((int) ((double) (this.width / 2) + (double) this.radius * Math.sin(al)), (int) ((double) (this.height / 2) - (double) this.radius * Math.cos(al)));
                if (this.cell > 0 && startRadius == (float) this.defaultStartAngle) {
                    this.firstPoint = this.tempPoint;
                }

                double angle = 6.283185307179586D * ((double) (startRadius + itemType.radius / 2.0F) / 360.0D);
                double sin = -Math.sin(angle);
                double cos = -Math.cos(angle);
                if (cos > 0.0D) {
                    this.leftTypeList.add(itemType);
                } else {
                    this.rightTypeList.add(itemType);
                }

                sumRadius += Math.abs(itemType.radius);
                this.mPaint.setStyle(Style.FILL);
                this.mPaint.setColor(itemType.color);
                if (this.pieCell > 0.0F) {
                    if (!(sumRadius <= this.offRadius)) {
                        this.mCanvas.drawArc(this.tempRectF, startRadius, itemType.radius - Math.abs(this.offRadius - sumRadius), true, this.mPaint);
                        break;
                    }

                    this.tempRectF.set(this.pieRectF.left - (float) ((double) this.pieCell * cos), this.pieRectF.top - (float) ((double) this.pieCell * sin), this.pieRectF.right - (float) ((double) this.pieCell * cos), this.pieRectF.bottom - (float) ((double) this.pieCell * sin));
                    this.mCanvas.drawArc(this.tempRectF, startRadius, itemType.radius, true, this.mPaint);
                } else {
                    if (!(sumRadius <= this.offRadius)) {
                        this.mCanvas.drawArc(this.pieRectF, startRadius, itemType.radius - Math.abs(this.offRadius - sumRadius), true, this.mPaint);
                        break;
                    }

                    this.mCanvas.drawArc(this.pieRectF, startRadius, itemType.radius, true, this.mPaint);
                }

                startRadius += itemType.radius;
                if (this.cell > 0 && this.pieCell == 0.0F) {
                    this.mPaint.setColor(this.backGroundColor);
                    this.mPaint.setStrokeWidth((float) this.cell);
                    this.mCanvas.drawLine((float) (this.getWidth() / 2), (float) (this.getHeight() / 2), (float) this.tempPoint.x, (float) this.tempPoint.y, this.mPaint);
                }
            }

            if (this.cell > 0 && this.firstPoint != null && this.pieCell == 0.0F) {
                this.mPaint.setColor(this.backGroundColor);
                this.mPaint.setStrokeWidth((float) this.cell);
                this.mCanvas.drawLine((float) (this.getWidth() / 2), (float) (this.getHeight() / 2), (float) this.firstPoint.x, (float) this.firstPoint.y, this.mPaint);
            }

            this.mPaint.setStyle(Style.FILL);
            this.mPaint.setColor(this.backGroundColor);
            if (this.innerRadius > 0.0F && this.pieCell == 0.0F) {
                this.mCanvas.drawCircle((float) (this.width / 2), (float) (this.height / 2), (float) this.radius * this.innerRadius, this.mPaint);
            }

        }
    }

    public void resetPaint() {
        this.mPaint.reset();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setAlpha(256);
    }

    public void addItemType(PieChartView.ItemType itemType) {
        if (this.itemTypeList != null) {
            this.itemTypeList.add(itemType);
        }

    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public void setInnerRadius(float innerRadius) {
        if (innerRadius > 1.0F) {
            innerRadius = 1.0F;
        } else if (innerRadius < 0.0F) {
            innerRadius = 0.0F;
        }

        this.innerRadius = innerRadius;
    }

    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public void setItemTextSize(int itemTextSize) {
        this.itemTextSize = itemTextSize;
    }

    public void setTextPadding(int textPadding) {
        this.textPadding = textPadding;
    }

    public void setAnimDuration(long animDuration) {
        this.animDuration = animDuration;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setPieCell(int pieCell) {
        this.cell = pieCell;
    }

    public static class ItemType {
        private static final DecimalFormat df = new DecimalFormat("0.0%");
        String type;
        int widget;
        int color;
        float radius;
        boolean isDrawText;

        public ItemType(String type, int widget, int color, boolean isDrawText) {
            this.type = type;
            this.widget = widget;
            this.color = color;
            this.isDrawText = isDrawText;
        }

        public String getPercent() {
            return df.format((double) (this.radius / 360.0F));
        }
    }
}
