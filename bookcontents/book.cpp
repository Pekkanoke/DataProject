/*
#############################################################################
# COMP.CS.110 Programming 2, Autumn 2022                                    #
# Project3: Book contents                                                   #
# File: book.cpp                                                            #
# Description: Methods to store and print data for the user. Does all the   #
#              commands that the interface asks for.                        #
#              Commands are: QUIT, IDS, CONTENTS, CLOSE <ID>, OPEN <ID>,    #
#              OPEN_ALL, PARENTS <ID> <N>, SUBCHAPTERS <ID> <N>,            #
#              SIBLINGS <ID>, LENGTH <ID>, LONGEST <ID>, SHORTEST <ID>      #
# Student number: 150290744                                                 #
# Name: Pekka Nokelainen                                                    #
# Email: pekka.nokelainen@tuni.fi                                           #
#############################################################################
*/

#include "book.hh"
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

Book::Book(): chapters_() {
}

Book::~Book(){

    for ( auto chapter : chapters_ ){
        delete chapter;
    }
}

void Book::addNewChapter(const std::string &id, const std::string &fullName,
                         int length){

    Chapter* new_chapter = new Chapter{};
    new_chapter->id_ = id;
    new_chapter->fullName_ = fullName;
    new_chapter->length_ = length;

    chapters_.push_back(new_chapter); // Adds on to the data structure.

}

void Book::addRelation(const std::string &subchapter,
                       const std::string &parentChapter){

    Chapter* sub = findChapter( subchapter );
    if ( parentChapter != EMPTY ){
        Chapter* parent = findChapter( parentChapter );
        sub->parentChapter_ = parent;
        parent->subchapters_.push_back( sub );
    }

}

void Book::printIds(Params params) const{

    for ( auto& i : params ){ // Don't know if I'm allowed to remove params so
        cout << i << endl;    // this is for the tester.
    }

    cout << "Book has " << chapters_.size() << " chapters:" << endl;

    vector<string> sorted; // Creating a list that can be sorted.
    for ( auto chapter : chapters_ ){
        sorted.push_back(chapter->fullName_);
    }

    sort(sorted.begin(), sorted.end());
    for ( auto& name : sorted){
        for ( auto chapter : chapters_){
            if ( chapter->fullName_ == name ){
                cout << chapter->fullName_ << " --- " << chapter->id_ << endl;
            }
        }
    }
}

void Book::recurivePrint(std::vector<Chapter *> allSubchapters, int level,
                         vector<string> usedChapters) const{

    level++; // level to keep track of white spaces in the print out.
    int count = 1; // Number to print in front of chapter.
    for ( auto chapter : allSubchapters ){

        bool printed = false; // Bool value to spot chapters that have been
                              // printed already.

        for ( auto& name : usedChapters ){
            if ( chapter->fullName_ == name ){
                printed = true;
            }
        }
        if ( printed ){ // If printed, loop moves to next.
            continue;
        }else{
            usedChapters.push_back( chapter->fullName_ ); // Adds to used
        }                                                 // chapters.

        // Different printouts for opened and closed chapters.
        if ( !chapter->open_ ){
            cout << "+ " << string(level * 2, ' ') << count << ". " <<
                    chapter->fullName_ <<" ( " << chapter->length_ << " )"
                 << endl;
        }else{
            cout << "- " << string(level * 2, ' ') << count << ". " <<
                    chapter->fullName_ <<" ( " <<
                    chapter->length_ << " )" << endl;
        }
        // Recursive call if needed.
        if ( chapter->subchapters_.size() != EMPTY_VECTOR && chapter->open_ ){
            recurivePrint(chapter->subchapters_, level, usedChapters);
        }
        count++;

    }
}

void Book::printContents(Params params) const{

    for ( auto& i : params ){ // Don't know if I'm allowed to remove params so
        cout << i << endl;    // this is for the tester.
    }

    int count = 0;
    vector<string> usedChapters = {};

    for ( auto chapter : chapters_ ){
        if ( chapter->parentChapter_ == nullptr ){
            ++count;

            // Different print outs for opened and closed chapters.
            if ( !chapter->open_ && chapter->subchapters_.size() !=
                 EMPTY_VECTOR){
                cout << "+ " << count << ". " << chapter->fullName_ <<" ( " <<
                        chapter->length_ << " )" << endl;

            }else{
                cout << "- " << count << ". " << chapter->fullName_ <<" ( " <<
                    chapter->length_ << " )" << endl;
            }
        }else{
            continue;
        }

        // Recursive call if needed.
        if ( chapter->subchapters_.size() != EMPTY_VECTOR && chapter->open_ ){
            recurivePrint(chapter->subchapters_, 0, usedChapters);
        }
    }
}

void Book::close(Params params) const{

    Chapter* chapter = findChapter(params[0]);
    if ( chapter == nullptr ){
        cout << "Error: Not found: " << params[0] << endl;
        return;
    }
    chapter->open_ = false;

}

void Book::open(Params params) const{

    bool found = false;
    for ( auto chapter : chapters_ ){
        if ( chapter->id_ == params[0] ){
            chapter->open_ = true;
            found = true;
            if ( chapter->subchapters_.size() != EMPTY_VECTOR ){
                for ( auto subChapter : chapter->subchapters_ ){
                    if ( subChapter->subchapters_.size() != EMPTY_VECTOR ){
                        subChapter->open_ = false;
                    }
                }
            }
        }
    }

    if ( !found ){
        cout << "Error: Not found: " << params[0] << endl;
    }
}

void Book::openAll(Params params) const{

    for ( auto& i : params ){ // Don't know if I'm allowed to remove params so
        cout << i << endl;    // this is for the tester.
    }

    for ( auto chapter : chapters_ ){
    chapter->open_ = true;
    }
}

void Book::printParentsN(Params params) const{

    int number = stoi(params[1]); // Input number to int.
    if ( number <= 0 ){
        cout << "Error. Level can't be less than 1." << endl;
        return;
    }

    int count = 0;
    Chapter* chapter;
    chapter = findChapter(params[0]);
    if ( chapter == nullptr ){
        cout << "Error: Not found: " << params[0] << endl;
        return;
    }

    vector<string> parentChapters; // List of parents that can be sorted.
    while ( chapter->parentChapter_ != nullptr && count < number){

        ++count;
        chapter = chapter->parentChapter_;
        parentChapters.push_back(chapter->id_);
    }

    if ( count == 0 ){
        cout << params[0] << " has no parent chapters." << endl;
        return;
    }
    sort(parentChapters.begin(), parentChapters.end());
    cout << params[0] << " has " << count << " parent chapters:" << endl;

    int index = 0;
    while ( index < count ){ // Sorted printout.

        cout << parentChapters[index] << endl;
        ++index;
    }
}

std::vector<string> Book::findSubChapters(vector<Chapter*>& chapters,
                                          vector<string> subChapters) const{

    vector<Chapter*> newChapters;
    // Loops throug one layer of sub chapters and adds them to vectors.
    for ( auto chapter : chapters ){
        for ( auto subChapter : chapter->subchapters_ ){
            newChapters.push_back(subChapter);
            subChapters.push_back(subChapter->id_);
        }
    }
    chapters = newChapters; // Changes chapter pointer vector to new sub
                            // chapters.
    return subChapters;
}

void Book::printSubchaptersN(Params params) const{

    int number = stoi(params[1]); // Interface input to int.
    if ( number <= 0 ){
        cout << "Error. Level can't be less than 1." << endl;
        return;
    }

    int level = 0;
    Chapter* chapter = findChapter(params[0]);
    if ( chapter == nullptr ){
        cout << "Error: Not found: " << params[0] << endl;
        return;
    }

    vector<Chapter*> chapters;
    chapters.push_back(chapter);
    vector<string> subChapters = {};

    // Loops through needed amount of sub chapter levels.
    while ( level < number ){
        if ( !chapters.empty() ){
            subChapters = findSubChapters(chapters, subChapters);
            ++level;
        }else{
            break;
        }
    }

    if ( !subChapters.empty() ){
        cout << chapter->id_ << " has " << subChapters.size()
             << " subchapters:" << endl;
    }else{
        cout << chapter->id_ << " has no subchapters." << endl;
    }
    sort(subChapters.begin(), subChapters.end()); // Sorting for printout.
    for ( auto& subChapter : subChapters ){
        cout << subChapter << endl;
    }

}

void Book::printSiblingChapters(Params params) const{

    Chapter* chapter = nullptr;
    // Looping all chapters to see if chapter exists.
    for ( auto oneChapter : chapters_ ){
        if ( oneChapter->id_ == params[0] ){
            chapter = oneChapter;
            if ( chapter->parentChapter_ == nullptr ){
                cout << chapter->id_ << " has no sibling chapters." << endl;
                return;
            }
        }
    }
    if ( chapter == nullptr ){
        cout << "Error: Not found: " << params[0] << endl;
        return;
    }

    vector<string> subChapters = {};

    for ( auto subChapter : chapter->parentChapter_->subchapters_ ){
        if ( subChapter->id_ != params[0] ){
            subChapters.push_back(subChapter->id_);
        }
    }
    cout << params[0] << " has " << subChapters.size() << " sibling chapters:"
                      << endl;
    sort(subChapters.begin(), subChapters.end());
    for ( auto& sibling : subChapters ){
        cout << sibling << endl;
    }
}

void Book::printTotalLength(Params params) const{

    // Initializing everything needed to call same method as other methods.
    Chapter* chapter = findChapter(params[0]);
    if ( chapter == nullptr ){
        cout << "Error: Not found: " << params[0] << endl;
        return;
    }

    vector<Chapter*> chapters;
    chapters.push_back(chapter);
    vector<string> subChapters = {};
    int totalLength = 0;
    totalLength += chapter->length_;

    while ( !chapters.empty() ){
        subChapters = findSubChapters(chapters, subChapters);
        for ( auto length : chapters ){
            totalLength += length->length_; // Adding lenghts together.
        }
    }

    cout << "Total length of " << params[0] << " is " << totalLength << "."
         << endl;
}

void Book::printLongestInHierarchy(Params params) const{

    // Initializing everything needed to call same method as other methods.
    Chapter* chapter = findChapter(params[0]);
    if ( chapter == nullptr ){
        cout << "Error: Not found: " << params[0] << endl;
        return;
    }

    vector<Chapter*> chapters;
    chapters.push_back(chapter);
    vector<string> subChapters = {};
    int longest = chapter->length_;
    string longestId = chapter->id_;

    while ( !chapters.empty() ){
        subChapters = findSubChapters(chapters, subChapters);
        for ( auto oneChapter : chapters ){
            if ( oneChapter->length_ > longest ){
                longest = oneChapter->length_;
                longestId = oneChapter->id_;
            }
        }
    }

    if ( params[0] == longestId ){
        cout << "With the length of " << longest << ", " << longestId
             << " is the longest chapter in their hierarchy." << endl;
    }else{
    cout << "With the length of " << longest << ", " << longestId
         << " is the longest chapter in " << params[0] << "'s hierarchy."
         << endl;
    }
}

void Book::printShortestInHierarchy(Params params) const{

    Chapter* chapter = findChapter(params[0]);
    if ( chapter == nullptr ){
        cout << "Error: Not found: " << params[0] << endl;
        return;
    }

    vector<Chapter*> chapters;
    chapters.push_back(chapter);
    vector<string> subChapters = {}; // Defined only so I can use same method
                                     // as others.
    int shortest = chapter->length_;
    string shortestId = chapter->id_;

    while ( !chapters.empty() ){
        subChapters = findSubChapters(chapters, subChapters);
        for ( auto oneChapter : chapters ){
            if ( oneChapter->length_ < shortest ){
                shortest = oneChapter->length_;
                shortestId = oneChapter->id_;
            }
        }
    }

    if ( params[0] == shortestId ){
        cout << "With the length of " << shortest << ", " << shortestId
             << " is the shortest chapter in their hierarchy." << endl;
    }else{
    cout << "With the length of " << shortest << ", " << shortestId
         << " is the shortest chapter in " << params[0] << "'s hierarchy."
         << endl;
    }
}

void Book::printParent(Params params) const{

    Chapter* chapter;
    chapter = findChapter(params[0]);

    cout << chapter->parentChapter_->id_ << endl;
}

void Book::printSubchapters(Params params) const{

    Chapter* chapter;
    chapter = findChapter(params[0]);

    for ( auto subChapter : chapter->subchapters_ ){
        cout << subChapter->id_ << endl;
    }
}

Chapter *Book::findChapter(const std::string &id) const{

    for ( auto chapter : chapters_ ){ // Loop to find pointer.
        if ( chapter->id_ == id ){

            return chapter;
        }
    }
    return nullptr;
}

