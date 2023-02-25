/*
#############################################################################
# COMP.CS.110 Programming 2: Autumn 2022                                    #
# Project3: Book contents                                                   #
# File: book.hh                                                             #
# Description: Class describing book chapter hierarchy                      #
#       Datastructure is populated with Chapter-structs and provides some   #
#       query-functions.                                                    #
# Notes: * This is a part of an exercise program                            #
#        * Student's aren't allowed to alter public interface!              #
#        * All changes to private side are allowed.                         #
# Student number: 150290744                                                 #
# Name: Pekka Nokelainen                                                    #
# Email: pekka.nokelainen@tuni.fi                                           #
#############################################################################
*/

#ifndef BOOK_HH
#define BOOK_HH

#include <string>
#include <vector>
#include <set>
#include <iostream>
#include <memory>

// Named constants to improve readability in other modules
const std::string EMPTY = "";
const int NO_LENGTH = -1;
const size_t EMPTY_VECTOR = 0;
const int MINIMUM_LEVEL = 0;

// Command parameters have been collected into a vector. In this way each
// method implementing a command has the same parameters (amount and type).
// If a command has no parameters, the vector is empty.
using Params = const std::vector<std::string>&;

// Struct for a book chapter
struct Chapter
{
    std::string id_ = EMPTY;
    std::string fullName_ = EMPTY;
    int length_ = 0;
    Chapter* parentChapter_ = nullptr;
    std::vector<Chapter*> subchapters_;
    bool open_ = true;
};

// Book class, a datastucture for Chapter structs
class Book
{
public:
    // Constructor
    Book();

    // Destructor
    ~Book();

    // Adds a new Chapter to the datastructure.
    void addNewChapter(const std::string& id, const std::string& fullName,
                       int length);

    // Adds a new chapter-subchapter relation.
    void addRelation(const std::string& subchapter,
                     const std::string& parentChapter);

    // Prints all stored chapters: ID, full name and length
    void printIds(Params params) const;

    // Prints the contents page of the book. Chapters are printed in the order,
    // they are in the book, subchapters are indented appropriately.
    // Either '-' or '+' is printed before each chapter, '-' for open chapters
    // and '+' for closed one. If a chapter is open, its subchapters are
    // also listed.
    void printContents(Params params) const;

    // Closes the given chapter and its subchapters.
    void close(Params params) const;

    // Opens the given chapter.
    void open(Params params) const;

    // Opens all chapters.
    void openAll(Params params) const;

    // Prints the amount and names of parent chapters in given distance from
    // the given chapter. Parent chapters are printed in alphabethical order.
    void printParentsN(Params params) const;

    // Prints the amount and names of subchapters in given distance from
    // the given chapter. Subchapters are printed in alphabethical order.
    void printSubchaptersN(Params params) const;

    // Prints the sibling chapters of the given chapter, i.e. the chapters
    // sharing the parent chapter with the given one.
    void printSiblingChapters(Params params) const;

    // Prints the total length of the given chapter, i.e. the sum of lengths
    // of the given chapter and its subchapters.
    void printTotalLength(Params params) const;

    // Prints the longest chapter in the hierarchy of the given chapter.
    void printLongestInHierarchy(Params params) const;

    // Prints the shortest chapter in the hierarchy of the given chapter.
    void printShortestInHierarchy(Params params) const;

    // Prints the direct parent chapter of the given chapter.
    // Students will not implement this method.
    void printParent(Params params) const;

    // Prints the direct subchapters of the given chapter.
    // Students will not implement this method.
    void printSubchapters(Params params) const;


private:
    /* The following functions are meant to make project easier.
     * You can implement them if you want and/or create your own.
     * Anyway it would be a good idea to implement more functions
     * to make things easier and to avoid "copy-paste-coding".
     */

    // Data structure.
    std::vector<Chapter*> chapters_;

    // Returns a pointer for ID.
    Chapter* findChapter(const std::string& id) const;

    // Help function for printContents to print all the chapters using a two
    // method recursive loop. Takes all sub chapters of a given chapter, the
    // counter of level and a list of used chapters.
    void recurivePrint(std::vector<Chapter*> allSubchapters, int level,
                       std::vector<std::string> usedChapters) const;

    // A help function to loop through all or as many as required of the sub
    // chapters. Returns a vector of names of the sub chapters, and changes
    // the vector of pointers to match with the pointers of the sub chapters.
    std::vector<std::string> findSubChapters(std::vector<Chapter*>& chapter,
                                             std::vector<std::string>
                                             subChapters) const;

};

#endif // BOOK_HH
