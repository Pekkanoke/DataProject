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
# File: mainwindow.hh                                              #
# Description: Declares a class representing the gui.              #
#                                                                  #
# Author: Pekka Nokelainen, 150290744, pekka.nokelainen@tuni.fi    #
####################################################################
*/

#ifndef MAINWINDOW_HH
#define MAINWINDOW_HH

#include <QMainWindow>
#include <QGraphicsEllipseItem>
#include <QGraphicsScene>
#include <QTimer>
#include "gameboard.hh"
#include <string>
#include <vector>
#include <utility>
#include <deque>
#include <QKeyEvent>

// Setting timer duration to one tenth of a second
const int DURATION = 100;
const int SNAKE_WIDTH = 10;
const int RGB_COLOR_SLIDE = 25;
const int SCALE = 10;

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();
    // Key event
    void keyPressEvent(QKeyEvent* event) override;

private slots:
    // When star button is clicked checks for player name and starts the game
    void onStartPushButtonClicked();

    // Scales the game zone to the values that the player has chosen. Can only
    // be done before the first game
    void onScaleButtonClicked();

    // Changes the direction to up if possible
    void onUpButtonClicked();

    // Changes the direction to left if possible
    void onLeftButtonClicked();

    // Changes the direction to right if possible
    void onRightButtonClicked();

    // Changes the direction to down if possible
    void onDownButtonClicked();

    // Move is called every time the timer goes off. Changes the timer,
    // checks if game is over and moves the snake on the screen
    void move();

    // Moves everything other than the head and the food
    void moveBody();

    // Resets the game to the begining
    void onResetPushButtonClicked();

    // Pauses the game if it is going and continues the game if it is paused
    void onPausePushButtonClicked();

    // Changes the value on the lcdNumber timers to match reality
    void addToTimer();

    // Adds score to the leaderboard
    void addScore();

private:

    // Pointers for the code to work
    Ui::MainWindow* ui;
    QGraphicsScene* scene_;
    QGraphicsRectItem* rectangle_;
    QGraphicsEllipseItem* foodIcon_;
    QTimer* timer_;

    // The pointer to the game being played, last is the one going on
    std::vector<GameBoard> games_;

    // Keeps track of the scores
    std::vector<std::pair<QString, int>> leaderboard_;

    // The graphicsView object is placed on the window
    // at the following coordinates:
    int leftMargin_ = 10; // x-coordinate
    int topMargin_ = 10; // y-coordinate

    // Gameboard size
    int borderRight_ = 540;
    int borderDown_ = 380;
    int borderUp_ = 0;
    int borderLeft_ = 0;

    // Seed value for the gameboard
    int seed_ = 0;

    // Player name
    QString name_;

    // Latest direction of the snake
    std::string direction_ = "w";

    // Snake and food
    std::vector<std::pair<int, int>> snake_;
    std::pair<int, int> food_;

    // Snakes lenght
    int lenght_ = 1;

    // Keeping track of the game
    bool paused_ = false;
    bool clicked_ = false;

    // Color for the RBG value
    int red_ = 255;
    int green_ = 0;
    int blue_ = 0;

    const int STEP = 10; // how much to move at a time

};
#endif // MAINWINDOW_HH
