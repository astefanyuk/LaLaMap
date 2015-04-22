/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mariko.animation;

import java.util.ArrayList;
import java.util.Collection;

public class AnimatorPath {

    // The points in the path
    ArrayList<PathPoint> mPoints = new ArrayList<PathPoint>();


    /**
     * Move from the current path point to the new one
     * specified by x and y. This will create a discontinuity if this point is
     * neither the first point in the path nor the same as the previous point
     * in the path.
     */
    public void moveTo(double x, double y) {
        mPoints.add(PathPoint.moveTo(x, y));
    }

    /**
     * Create a straight line from the current path point to the new one
     * specified by x and y.
     */
    public void lineTo(double x, double y) {
        mPoints.add(PathPoint.lineTo(x, y));
    }


    public void curveTo(double c0X, double c0Y, double c1X, double c1Y, double x, double y) {
        mPoints.add(PathPoint.curveTo(c0X, c0Y, c1X, c1Y, x, y));
    }

    /**
     * Returns a Collection of PathPoint objects that describe all points in the path.
     */
    public Collection<PathPoint> getPoints() {
        return mPoints;
    }
}
