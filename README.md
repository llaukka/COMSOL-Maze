# COMSOL-Maze
This repo is designed to be utilized in COMSOL (Computational Solutions) with the goal to generate a randomized maze according to user inputs. The code will be used in the application builder methods section. the main file contains everything one needs to set references to for the user inputs from the form and form objects.


1)
    Open COMSOL
    Model Wizard
    2D
    Finish (No Physics)
2)
    File
    Preferences
    General
    check Show Developer Tab
3)
    Model Builder <- RC Global Def <- parameters
    Add {
        hallway Thickness
        mazeHeight
        mazeWidth
        seed
    }
    Build All
4)
    Developer Tab <- Java Methods <- RC add java class
    name it. Must exactly match method call
    paste each method into a new method file in COMSOL (repeat for each of the 6 files)
    save
    inshalla accepts everything
    
5)
    RC model <- add form
    RC form <- input field X4 for each field {
        label: name
        source: parameter
        parameter name: (same from parameter)
    }
6)
    RC form <- button
    name it ...
    under on click <- method type <- java method <- select main()
7)
    click build all
    now generate maze


//instructions on how to run the COMSOL 
//simulations and interpret the results. Added a new
//section on troubleshooting common issues that //users
//may encounter when setting up their simulations. 
//Also included links to additional resources for 
//learning more about COMSOL and its applications in
maze-solving algorithms.