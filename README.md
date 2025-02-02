# SFWE301 Interactive Display Backend

## Description

This project served as the backend component for an interactive display application developed as part of a university course (SFWE301). It was written in Java and packaged via Maven, designed to complement an Electron-based front-end. The system showcases information about a university program or department by fetching live web data, generating embedded resources (PDFs, QR codes), and communicating them to the front-end.

## Table of Contents

- [Description](#description)
- [Contents](#contents)
- [Features](#features)
- [Technology](#technology)
- [Lessons](#lessons)
- [Overview / Retrospective](#overview--retrospective)

## Contents

- **sfwebackend/**  
  - **pom.xml**: Maven configuration for compiling, packaging, and managing dependencies.  
  - **src/main/java/com/sfwebackend/App.java**: Main entry point for the Java backend, performing web scraping, QR code generation, PDF manipulation, and triggering the front-end executable.  
  - **src/test/java/com/sfwebackend/AppTest.java**: Basic JUnit test demonstrating a unit test structure.  
  - **src/html/**: Contains HTML templates generated or filtered by the backend.  
  - **src/png/**: Stores generated QR code images.  
  - **src/pdf/**: Contains downloaded or processed PDF files.  
  - **target/**: Maven’s output directory for compiled classes and packaged JAR files.
- **SFWE301Backend.jar**  
  Pre-built JAR file for quick execution of the backend without manually running Maven commands.
- **README.txt**  
  Simple text note for setup instructions related to placing the executable (`sfwedisplay.exe`) alongside the backend files.

## Features

- **Web Scraping with JSoup**  
  Gathers program and department details from a specified university website, filtering out unwanted HTML elements to generate local pages.
- **QR Code Generation**  
  Creates QR code images linking users to external resources such as appointment booking pages or additional program materials.
- **PDF Fetching and Conversion**  
  Automatically retrieves PDF documents (e.g., four-year plan sheets) from a remote URL, then converts them to images if needed.
- **Launches Front-End Executable**  
  After preparing resources, the backend triggers an Electron-based front-end `.exe` to display the collected data interactively.
- **Modular Code Structure**  
  Organized via a Maven project structure that separates logic, resources, and tests.

## Technology

- **Programming Language:**  
  Java
- **Build & Dependency Management:**  
  Maven (with `pom.xml` for configuration)
- **Libraries & Frameworks:**  
  - [JSoup](https://jsoup.org/) for HTML parsing and web scraping  
  - [ZXing](https://github.com/zxing/zxing) for QR code generation  
  - [Spire.PDF](https://www.e-iceblue.com/Introduce/pdf-for-java.html) for PDF manipulation  
  - JUnit for testing
- **Concepts Demonstrated:**  
  - Web scraping and HTML filtering  
  - Automated resource generation (QR codes, PDFs, etc.)  
  - File I/O, image processing, and concurrency basics  
  - Use of external libraries in a Maven-based Java project

## Lessons

- **Web Scraping & Data Processing:**  
  Learned how to retrieve dynamic web content and refine it into user-friendly formats.
- **Working with External Libraries:**  
  Implemented PDF manipulation and QR code generation, improving skills in dependency management and library integration.
- **File Handling & Resource Management:**  
  Dealt with creating, deleting, and overwriting files in Java, ensuring the backend consistently produces updated artifacts.
- **Maven & Modular Project Organization:**  
  Gained hands-on experience structuring a large project with clear separation of concerns.
- **Integration with Electron Front-End:**  
  Practiced coordinating a Java backend with an external GUI, highlighting the importance of robust inter-process communication.

## Overview / Retrospective

- **What Went Well:**  
  - Successfully fetched and displayed real-time web data via the Electron front-end.  
  - Efficiently generated supporting materials (PDFs, QR codes, HTML) with minimal user intervention.  
  - Adopted a modular approach using Maven, making it simpler to package and share the project.
- **Areas for Improvement:**  
  - Improve code readability and modularity by separating out distinct operations (web scraping, PDF manipulation, etc.) into dedicated classes.  
  - Explore asynchronous or event-driven patterns for integrating real-time updates to the Electron front-end.
  - This just wasn't the best approach to the project we could've made, planning in advance and mapping out a distinctive backend/frontend interaction would've been the correct way to go, not using multiple executables and whatever you would call this - lmao.
- **Future Enhancements:**  
  - Expand functionality to scrape data from multiple sources or APIs.  
  - Implement a richer configuration system, allowing front-end users or administrators to customize displayed information easily.  
  - Develop advanced testing strategies (integration or end-to-end tests) to ensure stability in complex deployments.
