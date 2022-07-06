# model-learning-experiment
Below instructions are for running this project in the linux terminal.

## Running the API

### Requirements
- Python 3: e.g. `sudo apt-get install python3.6`
- Pip: e.g. `sudo apt-get install python3-pip python-dev`
- Flask: `pip install flask`
- Flask Restful: `pip install flask-restful`

### Run the API
From the root of the project, run the command: `python api/main.py` or `python3 api/main.py`


## Running the SUT

### Requirements
- Java JDK 11

### Run the SUT
From the root of the project, run the command: `java -jar sut/sut.jar`


## Running Tomte

### Requirements
- Tomte, see installation instructions: https://tomte.cs.ru.nl/Tomte-0-41/Install

### Run Tomte
From the root of the project, run the command: `tomte_learn config.yaml`
