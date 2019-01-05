# Semviz

[![Build Status](https://travis-ci.org/vmoglan/semviz.svg?branch=master)](https://travis-ci.org/vmoglan/semviz)

## Main objective
Create a viewer for semantically annotated three-dimensional point clouds.

## Description
### Semantic visualization
Semantic visualization consists of displaying the elements contained within an ontology. This ontology can be written using an `.owl` file or a triple store. The ontology is susceptible of containing a large number of individuals, so a SPARQL interpretor needs to be implemented in order to allow the user to reduce the number of individuals on display through filtering. By default, the displayed individuals will be those of class `Object` in the ontology. When one or more individual are selected, their information as well as their representation in the framework will be displayed in the 3D section of the interface.

### 3D visualization
The display of a 3D object must be done in three ways:

- Low resolution display in cubic form (octree)
- Polygon display (convex hull)
- High resolution (base point cloud)

## How to use
### Setup
You can download the Semviz all-in-one `.jar` at this [link](https://github.com/vmoglan/semviz/releases/download/v1.0.0/semviz-1.0.0.jar). There is no installation process, just make sure you have Java 8 installed on your marchine and then you should be able to run the file.
### Initialization
A **Semviz directory** is a folder that contains a number of point cloud files (for now only `.txt` holding `X Y Z R G B Normal0 Normal1 Normal2` information for each point is supported), as well as an ontology file gathering all ontological information about those clouds. 

> **NOTE:** For now, this program is developed to work with [Knowdip](https://www.researchgate.net/publication/317428247_Summary_of_KnowDIP_project), an internal project of the i3Mainz photogrammetry lab, so you cannot use Semviz as of now; I will ask for permission to provide a valid sample directory for demonstration purposes.

Once you have a valid Semviz directory, from the application's window you can open it by navigating to `File > Open` or hitting `Ctrl + O`. This process validates the structure of the directory and loads the ontology, so an error will be displayed if something went wrong loading.

### Queries
With the Semviz directory loaded (the red dot in the upper left side of the application turned green), the console bar at the bottom of the window should now be enabled. This bar is where the SPARQL queries for retrieving clouds are typed in; the SPARQL query **must** contain a `?cloud` variable and use the `knowdip` namespace, as in the example below:

```
select ?cloud ?size { ?cloud knowdip:hasSize ?size . }
```
The query above gets all clouds as well as their size information. The solutions given by the `?cloud` variable are listed within the _Cloud List_ view; below that, the _Queried Info_ table shows all the other variables under the key-value format.

### Display
Selecting a cloud within the Cloud List view will display it by default in its high resolution original colors form in the OpenGL canvas to the right. The _View_ drop-down list offers more cloud display options:

- High Resolution (Normals): this replaces the original values of the cloud with the values of its normals in each point;
- Convex Hull: displays the convex hull of the points in the cloud.
