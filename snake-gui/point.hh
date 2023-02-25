/*
####################################################################
# COMP.CS.110 Programming 2: Structures, 2022                      #
#                                                                  #
# Project4: Snake                                                  #
# Program description: Implements a game called Snake.             #
# The games objective is to eat as many apples as possible while   #
# staying inside the green game zone and trying not to crash into  #
# itself                                                           #
#                                                                  #
# File: point.hh                                                   #
# Description: Declares a class representing a point in            #
# the gameboard.                                                   #
#                                                                  #
# Author: Pekka Nokelainen, 150290744, pekka.nokelainen@tuni.fi    #
####################################################################
*/

#ifndef POINT_HH
#define POINT_HH

#include <string>

// Constants for directions
const char UP = 'w';
const char LEFT = 'a';
const char DOWN = 's';
const char RIGHT = 'd';

class Point
{
public:
    // Default constructor, creates a new point at (0, 0).
    Point();

    // Constructor, creates a new point at the given coordinates.
    Point(int x, int y);

    // Destructor
    ~Point();

    // Comparison operators
    bool operator==(const Point& rhs) const;
    bool operator!=(const Point& rhs) const;

    // Sets a new position for the point
    void setPosition(int x, int y);

    // Moves the point into the given direction, direction can be one of
    // constants above.
    void move(const std::string& dir);

    // Tells if the point is inside the square specified by the given
    // parameters.
    bool isInside(int left_top_x, int left_top_y,
                  int right_bottom_x, int right_bottom_y) const;

    // Returns pair of coordinates to the snakes head.
    std::pair<int, int> coordinates();

private:
    // Coordinates of the point
    int x_;
    int y_;
};

#endif // POINT_HH
