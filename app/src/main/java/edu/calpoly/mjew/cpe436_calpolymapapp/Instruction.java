package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.graphics.drawable.Drawable;
import android.media.Image;

/**
 * Created by mackenzie on 11/26/16.
 * Meant to represent a single instruction that may be part of a set. Should be part of an array
 * stored in a Route class.
 */
public class Instruction
{
    private String mStepText; //the text for this particular step in the entire route.
    private Drawable mImage;

    public Instruction(String newText, Drawable newImage)
    {
        mStepText = newText;
        mImage = newImage;
    }

    public String getText() { return mStepText; }

    public Drawable getImage() { return mImage; }
}
