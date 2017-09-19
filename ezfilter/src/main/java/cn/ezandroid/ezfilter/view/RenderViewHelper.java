package cn.ezandroid.ezfilter.view;

/**
 * 包含布局辅助类
 * 负责根据旋转角和宽高比等计算预览区域大小
 *
 * @author like
 * @date 2017-08-02
 */
public class RenderViewHelper {

    private float mAspectRatio = 1.0f;

    private int mMaxWidth;
    private int mMaxHeight;

    private int mAngle;

    private int mPreviewWidth;
    private int mPreviewHeight;

    private ScaleType mScaleType = ScaleType.CENTER_INSIDE;

    public enum ScaleType {
        CENTER_CROP,
        CENTER_INSIDE
    }

    /**
     * 设置缩放规则
     *
     * @param scaleType 缩放规则
     */
    public void setScaleType(ScaleType scaleType) {
        mScaleType = scaleType;
    }

    /**
     * 设置宽高比及最大宽度最大高度
     * maxWidth和maxHeight有一个设置为0时表示MatchParent
     *
     * @param ratio     宽高比
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return 是否应该调用requestLayout刷新视图
     */
    public boolean setAspectRatio(float ratio, int maxWidth, int maxHeight) {
        if (ratio <= 0.0 || maxWidth < 0 || maxHeight < 0) throw new IllegalArgumentException();
        if (mAspectRatio != ratio
                || mMaxWidth != maxWidth
                || mMaxHeight != maxHeight) {
            mAspectRatio = ratio;
            mMaxWidth = maxWidth;
            mMaxHeight = maxHeight;

            return calculatePreviewSize(0, 0);
        }
        return false;
    }

    /**
     * 设置顺时针旋转90度的次数
     * 取值-3~0，0~3，表示旋转-270~0，0~270度
     *
     * @param numOfTimes 旋转次数
     * @return 是否应该调用requestLayout刷新视图
     */
    public boolean setRotate90Degrees(int numOfTimes) {
        if (mAngle != 90 * numOfTimes) {
            while (numOfTimes < 0) {
                numOfTimes = 4 + numOfTimes;
            }
            mAngle = 90 * numOfTimes;
            return calculatePreviewSize(0, 0);
        }
        return false;
    }

    /**
     * 获取宽高比
     *
     * @return
     */
    public float getAspectRatio() {
        return mAspectRatio;
    }

    /**
     * 获取顺时针旋转90度的次数
     *
     * @return
     */
    public int getRotation90Degrees() {
        return mAngle / 90;
    }

    /**
     * 获取预览宽度
     *
     * @return
     */
    public int getPreviewWidth() {
        return mPreviewWidth;
    }

    /**
     * 获取预览高度
     *
     * @return
     */
    public int getPreviewHeight() {
        return mPreviewHeight;
    }

    /**
     * 计算预览区域大小
     *
     * @param measureWidth
     * @param measureHeight
     * @return 是否应该调用requestLayout刷新视图
     */
    protected boolean calculatePreviewSize(int measureWidth, int measureHeight) {
        int previewWidth = 0;
        int previewHeight = 0;
        if (mScaleType == ScaleType.CENTER_INSIDE) {
            if (mAngle / 90 % 2 == 1) {
                if (mMaxWidth != 0 && mMaxHeight != 0) {
                    previewWidth = mMaxWidth;
                    previewHeight = mMaxHeight;
                    if (previewHeight > previewWidth * mAspectRatio) {
                        previewHeight = (int) (previewWidth * mAspectRatio + .5);
                    } else {
                        previewWidth = (int) (previewHeight / mAspectRatio + .5);
                    }
                } else {
                    previewWidth = measureWidth;
                    previewHeight = measureHeight;
                    if (previewHeight > previewWidth * mAspectRatio) {
                        previewHeight = (int) (previewWidth * mAspectRatio + .5);
                    } else {
                        previewWidth = (int) (previewHeight / mAspectRatio + .5);
                    }
                }
            } else {
                if (mMaxWidth != 0 && mMaxHeight != 0) {
                    previewWidth = mMaxWidth;
                    previewHeight = mMaxHeight;
                    if (previewWidth > previewHeight * mAspectRatio) {
                        previewWidth = (int) (previewHeight * mAspectRatio + .5);
                    } else {
                        previewHeight = (int) (previewWidth / mAspectRatio + .5);
                    }
                } else {
                    previewWidth = measureWidth;
                    previewHeight = measureHeight;
                    if (previewWidth > previewHeight * mAspectRatio) {
                        previewWidth = (int) (previewHeight * mAspectRatio + .5);
                    } else {
                        previewHeight = (int) (previewWidth / mAspectRatio + .5);
                    }
                }
            }
        } else {
            if (mAngle / 90 % 2 == 1) {
                if (mMaxWidth != 0 && mMaxHeight != 0) {
                    if (mAspectRatio > mMaxHeight * 1.0f / mMaxWidth) {
                        previewHeight = (int) (mMaxWidth * mAspectRatio + .5);
                        previewWidth = mMaxWidth;
                    } else {
                        previewWidth = (int) (mMaxHeight / mAspectRatio + .5);
                        previewHeight = mMaxHeight;
                    }
                } else if (measureWidth != 0 && measureHeight != 0) {
                    if (mAspectRatio > measureHeight * 1.0f / measureWidth) {
                        previewHeight = (int) (measureWidth * mAspectRatio + .5);
                        previewWidth = measureWidth;
                    } else {
                        previewWidth = (int) (measureHeight / mAspectRatio + .5);
                        previewHeight = measureHeight;
                    }
                }
            } else {
                if (mMaxWidth != 0 && mMaxHeight != 0) {
                    if (mAspectRatio > mMaxWidth * 1.0f / mMaxHeight) {
                        // 填充高度
                        previewHeight = mMaxHeight;
                        previewWidth = (int) (mMaxHeight * mAspectRatio + .5);
                    } else {
                        // 填充宽度
                        previewWidth = mMaxWidth;
                        previewHeight = (int) (mMaxWidth / mAspectRatio + .5);
                    }
                } else if (measureWidth != 0 && measureHeight != 0) {
                    if (mAspectRatio > measureWidth * 1.0f / measureHeight) {
                        // 填充高度
                        previewHeight = measureHeight;
                        previewWidth = (int) (measureHeight * mAspectRatio + .5);
                    } else {
                        // 填充宽度
                        previewWidth = measureWidth;
                        previewHeight = (int) (measureWidth / mAspectRatio + .5);
                    }
                }
            }
        }

        boolean change = previewWidth != mPreviewWidth || previewHeight != mPreviewHeight;
        mPreviewWidth = previewWidth;
        mPreviewHeight = previewHeight;
//        Log.e("RenderViewHelper", "calculatePreviewSize:" + mPreviewWidth + "x" + mPreviewHeight);
        return change;
    }
}
