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
# File: mainwindow.cpp                                             #
# Description: Defines a class representing the gui.               #
#                                                                  #
# Author: Pekka Nokelainen, 150290744, pekka.nokelainen@tuni.fi    #
####################################################################
*/

#include "mainwindow.hh"
#include "ui_mainwindow.h"
#include "gameboard.hh"

#include <QDebug>
#include <QTimer>
#include <QKeyEvent>
#include <QGraphicsEllipseItem>
#include <iostream>
#include <vector>
#include <iterator>
#include <algorithm>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setStyleSheet("QMainWindow { background-color: lightgrey; }");

    // Setting min and max values to size sliders
    ui->widthSlider->setMinimum(10);
    ui->widthSlider->setMaximum(54);

    ui->heightSlider->setMinimum(10);
    ui->heightSlider->setMaximum(38);

    ui->widthSlider->setValue(54);
    ui->heightSlider->setValue(38);

    // Set up lcd numbers style
    ui->lcdTenthSeconds->setSegmentStyle(QLCDNumber::Flat);
    ui->lcdTime->setSegmentStyle(QLCDNumber::Flat);
    ui->pointsLCD->setSegmentStyle(QLCDNumber::Flat);
    ui->lcdTenthSeconds->setStyleSheet("background-color:burlywood;");
    ui->lcdTime->setStyleSheet("background-color:coral;");
    ui->pointsLCD->setStyleSheet("background-color:white;");

    connect(ui->resetPushButton, SIGNAL(clicked()),
            this, SLOT(onResetPushButtonClicked()));
    connect(ui->pausePushButton, SIGNAL(clicked()),
            this, SLOT(onPausePushButtonClicked()));
    connect(ui->startPushButton, SIGNAL(clicked()),
            this, SLOT(onStartPushButtonClicked()));
    connect(ui->scaleButton, SIGNAL(clicked()),
            this, SLOT(onScaleButtonClicked()));

    connect(ui->upButton, SIGNAL(clicked()), this, SLOT(onUpButtonClicked()));
    connect(ui->downButton, SIGNAL(clicked()),
            this, SLOT(onDownButtonClicked()));
    connect(ui->rightButton, SIGNAL(clicked()),
            this, SLOT(onRightButtonClicked()));
    connect(ui->leftButton, SIGNAL(clicked()),
            this, SLOT(onLeftButtonClicked()));

    // Sets reset button disabled
    ui->resetPushButton->setDisabled(true);

    // Sets pause button disabled
    ui->pausePushButton->setDisabled(true);

    // We need a graphics scene in which to draw
    scene_ = new QGraphicsScene(this);

    // The width of the graphicsView is BORDER_RIGHT added by 2,
    // since the borders take one pixel on each side
    // (1 on the left, and 1 on the right).
    // Similarly, the height of the graphicsView is BORDER_DOWN added by 2.
    ui->gameBoardGraphicsView->setGeometry(leftMargin_, topMargin_,
                                   borderRight_ + 2, borderDown_ + 2);
    ui->gameBoardGraphicsView->setScene(scene_);

    // The width of the scene_ is BORDER_RIGHT decreased by 1 and
    // the height of it is BORDER_DOWN decreased by 1,
    // because the snake is considered to be inside the sceneRect,
    // if its upper left corner is inside the sceneRect.
    scene_->setSceneRect(0, 0, borderRight_, borderDown_);

    // Sets the background color.
    scene_->setBackgroundBrush(Qt::darkGreen);

    // Hide game over label
    ui->gameOverLabel->setText("");

    QBrush blackBrush(Qt::black);
    QPen outlinePen(Qt::black);

    // Difines and places the head of the snake
    rectangle_ = scene_->addRect((borderRight_ - 1) / 2, (borderDown_ - 1) / 2,
                                 SNAKE_WIDTH, SNAKE_WIDTH,
                                 outlinePen, blackBrush);
    rectangle_->setFlags(QGraphicsItem::ItemIsMovable |
                         QGraphicsItem::ItemIsSelectable);
    rectangle_->setZValue(2); // Will always be displayed on top background
    rectangle_->setPos(-SNAKE_WIDTH, -SNAKE_WIDTH);

    // Difines and places the food
    QBrush redBrush(Qt::red);
    QPen redPen(Qt::red);
    redPen.setWidth(2);
    foodIcon_ = scene_->addEllipse(-STEP, -STEP, STEP, STEP, redPen, redBrush);
    foodIcon_->setFlags(QGraphicsItem::ItemIsMovable |
                        QGraphicsItem::ItemIsSelectable);

    timer_ = new QTimer(this);
    // A non-singleshot timer fires every interval
    // (DURATION much milliseconds), which makes circle_move function
    // to be called until the timer is stopped
    timer_->setSingleShot(false);
    connect(timer_, &QTimer::timeout, this, &MainWindow::move);

    // Set numbers on the leaderboard empty
    ui->numberOne->setText("1. ");
    ui->numberTwo->setText("2. ");
    ui->numberThree->setText("3. ");
}

MainWindow::~MainWindow()
{
    delete timer_;
    delete scene_;
    delete ui;
}

void MainWindow::keyPressEvent(QKeyEvent *event)
{
    if ( event->key() == Qt::Key_W ){
        onUpButtonClicked();
    }else if ( event->key() == Qt::Key_S ){
        onDownButtonClicked();
    }else if ( event->key() == Qt::Key_A ){
        onLeftButtonClicked();
    }else if ( event->key() == Qt::Key_D ){
        onRightButtonClicked();
    }
}


void MainWindow::onStartPushButtonClicked()
{
    // Checks for player name input
    if ( ui->playerNameLineEdit->text() == "" ){
        ui->enterNameLabel->setStyleSheet("QLabel { color : red; }");
        return;
    }else{
        ui->enterNameLabel->setStyleSheet("QLabel { color : black; }");
        name_ = ui->playerNameLineEdit->text();
    }
    // Gets the seed value to start the game
    seed_ = ui->seedValueBox->value();
    // Sets reset and pause button on
    ui->resetPushButton->setDisabled(false);
    ui->pausePushButton->setDisabled(false);
    ui->seedValueBox->setDisabled(true);
    ui->playerNameLineEdit->setDisabled(true);

    GameBoard game((borderRight_ / SCALE), (borderDown_ / SCALE), seed_);
    games_.push_back(game);

    // Starts the timer
    ui->startPushButton->setDisabled(true);
    timer_->start(DURATION);

    // The gameboard size cannot be changed after first game is started
    ui->scaleButton->setDisabled(true);
}

void MainWindow::onScaleButtonClicked()
{
    // Checks slider values
    borderRight_ = ui->widthSlider->value() * SCALE;
    borderDown_ = ui->heightSlider->value() * SCALE;

    // Updates the gameboard
    ui->gameBoardGraphicsView->setGeometry(leftMargin_, topMargin_,
                                   borderRight_ + 2, borderDown_ + 2);
    ui->gameBoardGraphicsView->setScene(scene_);
    scene_->setSceneRect(0, 0, borderRight_, borderDown_);

    rectangle_->setRect((borderRight_ - 1) / 2, (borderDown_ - 1) / 2,
                        SNAKE_WIDTH, SNAKE_WIDTH);
    rectangle_->setPos(-SNAKE_WIDTH, -SNAKE_WIDTH);
}


void MainWindow::move()
{
    clicked_ = false;

    // Update points
    ui->pointsLCD->display( lenght_ - 1 );

    // Add to timer
    addToTimer();

    food_ = games_.back().coordinatesFood();
    bool hasMoved = games_.back().moveSnake(direction_);

    // If game has ended
    if ( !hasMoved ){
        // Stops the timer
        timer_->stop();
        // Adds the score to the scoreboard.
        addScore();
        // Prints suitable text
        QFont f( "Arial", 64, QFont::Bold);
        ui->gameOverLabel->setFont( f );
        if ( games_.back().gameWon() ){
            ui->gameOverLabel->setStyleSheet("QLabel { color : gold; }");
            ui->gameOverLabel->setText("You Won!");
            this->setStyleSheet("QMainWindow { background-color: green; }");
        }else{
            ui->gameOverLabel->setStyleSheet("QLabel { color : white; }");
            ui->gameOverLabel->setText("You Lost");
            this->setStyleSheet("QMainWindow { background-color: red; }");
        }
    }

    snake_.push_back(games_.back().coordinatesHead());

    // Determines if the snake grows or stays the same length
    bool grow = true;
    if ( food_ == games_.back().coordinatesFood() ){
        grow = false;
    }
    if ( grow ){
        lenght_ += 1;
    }

    std::string::size_type size = lenght_;
    while ( snake_.size() > size + 1 ){ // Plus one so the last part is deleted
        snake_.erase(snake_.begin());
    }

    // Sets the food to the right place
    food_ = games_.back().coordinatesFood();
    int xFood = food_.first * SCALE + 10;
    int yFood = food_.second * SCALE + 10;
    foodIcon_->setPos(xFood, yFood);
    foodIcon_->setZValue(2);

    // Sets the snakes head to the right place
    int xHead = snake_.back().first * SCALE - ( (borderRight_ - 1) / 2);
    int yHead = snake_.back().second * SCALE - ( (borderDown_ - 1) / 2);
    rectangle_->setPos(xHead, yHead);
    rectangle_->setBrush(Qt::black);

    moveBody();
}

void MainWindow::moveBody()
{
    // Sets the snakes body to the right place
    std::string::size_type size = lenght_;
    std::vector<std::pair<int, int>> snake = snake_;

    // Changes the part after the last part to background color
    for ( auto pair : snake ){
        if ( snake.size() > size ){
            int x = pair.first * SCALE;
            int y = pair.second * SCALE;
            QGraphicsItem* item = scene_->addRect(x, y, SNAKE_WIDTH,
                                                  SNAKE_WIDTH, Qt::NoPen,
                                                  Qt::darkGreen);
            item->setZValue(1);

            snake.erase(snake.begin());
        }
    }

    snake.pop_back(); // Remove head so it is not repainted
    std::reverse(snake.begin(), snake.end()); // Flips around so new color is
                                              // added to the tail
    // Paints the body with sliding colors.
    for ( auto part : snake ){

        int x = part.first * SCALE;
        int y = part.second * SCALE;

        // White tail disabled
        /*if ( part == snake.back() ){
            QBrush whiteBrush(Qt::white);
            QGraphicsItem* item = scene_->addRect(x, y, SNAKE_WIDTH,
                                                  SNAKE_WIDTH, Qt::NoPen,
                                                  whiteBrush);
            item->setZValue(1);
        }else{*/
        QBrush *myBrush = new QBrush(QColor(red_, green_, blue_));
        QGraphicsItem* item = scene_->addRect(x, y, SNAKE_WIDTH, SNAKE_WIDTH,
                                              Qt::NoPen, *myBrush);
        delete myBrush;
        item->setZValue(1);
        //}
        // Changes the snakes color with sliding coloring.
        if ( green_ < 250 ){
            green_ += RGB_COLOR_SLIDE;
        }else if ( green_ >= 250 && red_ <= 5 ){
            if ( blue_ > 5 ){
                blue_ -= RGB_COLOR_SLIDE;
            }else{
                green_ = 255;
                red_ = 0;
                blue_ = 0;
            }
        }else if ( blue_ < 250 ){
            blue_ += RGB_COLOR_SLIDE;
        }else if ( red_ > 5 ){
            red_ -= RGB_COLOR_SLIDE;
        }
    }
    // Sets the colors back to original for next print out
    red_ = 255;
    green_ = 0;
    blue_ = 0;
}

void MainWindow::onUpButtonClicked()
{
    if ( clicked_ ){
        return;
    }else{
        clicked_ = true;
    }
    // Does nothing if game is paused
    if ( paused_ ){
        return;
    }
    // Changes direction
    if ( direction_ != "s" ){
        direction_ = "w";
    }
}

void MainWindow::onLeftButtonClicked()
{
    if ( clicked_ ){
        return;
    }else{
        clicked_ = true;
    }
    // Does nothing if game is paused
    if ( paused_ ){
        return;
    }
    // Changes direction
    if ( direction_ != "d" ){
        direction_ = "a";
    }
}

void MainWindow::onRightButtonClicked()
{
    if ( clicked_ ){
        return;
    }else{
        clicked_ = true;
    }
    // Does nothing if game is paused
    if ( paused_ ){
        return;
    }
    // Changes direction
    if ( direction_ != "a" ){
        direction_ = "d";
    }
}

void MainWindow::onDownButtonClicked()
{
    if ( clicked_ ){
        return;
    }else{
        clicked_ = true;
    }
    // Does nothing if game is paused
    if ( paused_ ){
        return;
    }
    // Changes direction
    if ( direction_ != "w" ){
        direction_ = "s";
    }
}

void MainWindow::onResetPushButtonClicked()
{
    // Removes the snake
    for ( auto pair : snake_ ){
        int x = pair.first * SCALE;
        int y = pair.second * SCALE;
        QGraphicsItem* item = scene_->addRect(x, y, SNAKE_WIDTH, SNAKE_WIDTH,
                                              Qt::NoPen, Qt::darkGreen);
        item->setZValue(1);
    }

    // Resets the direction, score and length
    direction_ = "w";
    lenght_ = 1;
    ui->pointsLCD->display(0);

    // Sets background color back to normal
    this->setStyleSheet("QMainWindow { background-color: lightgrey; }");

    // Sets victory or defeat label text to blank
    ui->gameOverLabel->setText("");

    // Places head to the center
    rectangle_->setPos(-SNAKE_WIDTH, -SNAKE_WIDTH);

    // Turns start button back on
    ui->startPushButton->setDisabled(false);

    // Changes timer back to zero
    ui->lcdTime->display(0);

    // Sets reset button as disabled
    ui->resetPushButton->setDisabled(true);

    if ( timer_->isActive() ){
        timer_->stop();
    }

    ui->seedValueBox->setDisabled(false);
    ui->playerNameLineEdit->setDisabled(false);
}

void MainWindow::onPausePushButtonClicked()
{
    // If game is over does nothing
    if ( games_.back().gameOver() ){
        return;
    }
    // Stops or strarts the timer
    if ( timer_->isActive() ){
        paused_ = true;
        timer_->stop();

        // Change text to continue
        ui->pausePushButton->setText("Continue");
        // Change label text
        ui->gameOverLabel->setStyleSheet("QLabel { color : black; }");
        ui->gameOverLabel->setText("Game Paused");
    }else{
        timer_->start(DURATION);
        paused_ = false;

        // Change text back to pause
        ui->pausePushButton->setText("Pause");
        // Change label to blank
        ui->gameOverLabel->setText("");
    }
}

void MainWindow::addToTimer()
{
    if ( ui->lcdTenthSeconds->value() == 10 ){ // Adds one second
        ui->lcdTime->display(ui->lcdTime->value() + 1);
        ui->lcdTenthSeconds->display(0);
    }else{ // Adds to tenth seconds
        ui->lcdTenthSeconds->display(ui->lcdTenthSeconds->value() + 1);
    }
}

void MainWindow::addScore()
{
    // Adds score to the scoreboard
    leaderboard_.push_back(std::make_pair(name_, lenght_ - 1));

    std::vector<int> sortVector;

    for ( auto& player : leaderboard_ ){
        sortVector.push_back(player.second);
    }

    sort(sortVector.begin(), sortVector.end());

    std::string::size_type size = 3;
    while( sortVector.size() > size ){
        sortVector.erase(sortVector.begin());
    }

    std::string::size_type two = 2;
    for ( auto& pair : leaderboard_ ){
        if ( size > leaderboard_.size() ){ // Checks if there is less than 3
                                           // players on the leaderboard
            if ( two > leaderboard_.size() ){
                ui->numberOne->setText("1. " + pair.first);
                ui->onePoints->setNum(pair.second);
            }else{
                if( pair.second == sortVector[0] ){
                    ui->numberTwo->setText("2. " + pair.first);
                    ui->twoPoints->setNum(pair.second);

                }else {
                    ui->numberOne->setText("1. " + pair.first);
                    ui->onePoints->setNum(pair.second);
                }
            }
            continue;
        }

        // Normal sorting of a leaderboard
        if ( pair.second == sortVector[0]){
            ui->numberThree->setText("3. " + pair.first);
            ui->threePoints->setNum(pair.second);

        }else if( pair.second == sortVector[1]){
            ui->numberTwo->setText("2. " + pair.first);
            ui->twoPoints->setNum(pair.second);

        }else if( pair.second == sortVector[2] ){
            ui->numberOne->setText("1. " + pair.first);
            ui->onePoints->setNum(pair.second);
        }
    }
}
