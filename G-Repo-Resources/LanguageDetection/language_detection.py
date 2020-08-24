"""
LANGUAGE DETECTOR SCRIPT
Author: Anas Mounsif - Università Degli Studi Della Basilicata

Requirements:
six
Python 3.8
Probably glob, ConfigParser

Usage:
[ python3 language_detection.py ] or [ python3 language_detection.py -h ] for more information.

* Consult config.ini and log.conf
"""

# ----------------------------------------------------------------------------------------------------------------------
# IMPORTS
# ----------------------------------------------------------------------------------------------------------------------
# Importing Libraries
import os
import re
import csv
import time
import shutil as sh
import logging.config
import langdetect as dl
from pathlib import Path
from datetime import date
from glob import glob as gl
from typing import Tuple, Optional
from configparser import ConfigParser

# Importing Exceptions from Libraries
from langdetect.lang_detect_exception import LangDetectException

# ----------------------------------------------------------------------------------------------------------------------
# ABOUT AUTHOR
# ----------------------------------------------------------------------------------------------------------------------
# Creates problem with pytest --cov=module_name command
'''
import argparse

# Colors Configuration
_RED = '\033[91m'
_END = '\033[0m'

# Defining the script description
_DESCRIPTION = ' This script is able to filter repositories based on the language used for writing the README. ' \
               '\n Use the normal language translation library found at this address: ' \
               '{}https://github.com/Mimino666/langdetect{}' \
               '\n And implements regular expressions for cleaning the files to be translated from the markdowns.' \
               '\n At the end it generates a CSV file with all the information inside.' \
               '\n It also generates a log file where it is possible to consult the operations carried out ' \
               'as well as identify any problems. ' \
               '\n Use the {}config.ini{} to meet your needs.' \
               '\n\n For more information about this script see: ' \
               '{}https://github.com/anasmounsif/README-language-detector{}'.format(_RED, _END, _RED, _END, _RED, _END)

# Defining the script About Author
_ABOUT = " {}Anas Mounsif{} - University of Basilicata.".format(_RED, _END)

# Initiating the parser with a description
parser = argparse.ArgumentParser(description=_DESCRIPTION, formatter_class=argparse.RawDescriptionHelpFormatter)

# Adding optional argument
parser.add_argument("-v", "--version", help=" show script version", action='version', version=' version: 1.0.8')
parser.add_argument("-a", "--author", help=" show author information", action='version', version=_ABOUT)
parser.parse_args()
'''

# ----------------------------------------------------------------------------------------------------------------------
# PREFERENCES
# ----------------------------------------------------------------------------------------------------------------------
# Load the configuration file - Make sure config.ini exists!
parser = ConfigParser()
parser.read('config.ini')

# Set to 0 for deterministic result, 1 for non-deterministic in config.ini
_OUTPUT_TYPE = 0 if int(parser.get("parameters", "translation_type")) == 0 else 1

# Set to 1 for output generation in config.ini, 0 for not
_CSV_OUTPUT = 0 if int(parser.get("files", "activate_csv_output")) == 0 else 1

# Set to 1 for INPUT generation in config.ini, 0 for not, and get path
_INPUT_GENERATOR = 0 if int(parser.get("files", "input_generator")) == 0 else 1
_REPOSITORIES_PATH = Path(str(parser.get("files", "repository_path")))

# Set min and max percentages for english checks
_EN_MIN = float(parser.get("percentages", "en_min"))
_EN_MAX = float(parser.get("percentages", "en_max"))

# True if you want the script to move the repositories else False in config.ini - be care to ACCESS DENIED error!
_MOVE = False if int((parser.get("parameters", "moving_feature"))) == 0 else True

# Modify as needed in config.ini, but pay attention to folder's structure
_ENGLISH = Path(str(parser.get("destinations", "destination_english")))
_NOT_ENGLISH = Path(str(parser.get("destinations", "destination_not_english")))
_MIXED = Path(str(parser.get("destinations", "destination_mixed")))
_UNKNOWN = Path(str(parser.get("destinations", "destination_unknown")))

# CSV config params
_CSV = str(parser.get("files", "input"))  # Get input csv name from config.ini
_NEW_CSV = str(parser.get("files", "output"))  # Get output csv name from config.ini
_MIN_LENGTH = int(parser.get("parameters", "min_length"))  # Get min length for translation from config.ini
_FIELDNAMES = ['Index', 'CloneDirectory', 'ReadmeAnalyzed', 'Language',
               'Code1', 'Percentage1', 'Code2', 'Percentage2']  # Fieldnames structure of output.csv
_INPUT_FIELDNAMES = ['Index', 'CloneDirectory']  # Fieldnames structure of input.csv

# Regex patterns
# _WARNING = r"[^A-Za-z0-9]"  # pattern to recognize all special characters - Also delete the Chinese Character!
_TABLES = r"^(\|[^\n]+\|\r?\n)((?:\|:?[-]+:?)+\|)(\n(?:\|[^\n]+\|\r?\n?)*)?$"  # Recognize tables
_REMAINING_SPECIAL_CHARS = r"([#@\s]|:[)(])|\W"  # Recognize all special characters
_ILLEGAL_STRING = r'^[_\W0-9]+$'  # Recognize the only digits or special character
_URLS = r"https?:\/\/?[\da-z\.-]+\.[a-z\.]{2,6}[\/\w \.-]*"  # Recognize urls
_CODE_SNIPPETS = r"(```.+?```)"  # Recognize code snippets
_IMAGES = r"!\[[^\]]+\]\([^)]+\)"  # Recognize images
_HTML = r"\<.*?\>"  # Recognize the html code
_LINKS = r"\[.*?\]\(.*?\)"  # Recognize links

# Supported extension
_EXTENSIONS = ['*.markdown', '*.mkdn', '*.md', '*.mdown', '*.txt', '*.mdwn', '*.mkd']

# Init logger
logging.config.fileConfig(fname='log.conf', defaults={'logfilename': 'log'})
logger = logging.getLogger('sessionLogs')

# Config shortcut
MOVE_TO = sh.move
PATH = os.path
SEPARATOR = os.sep
TODAY = date.today()

# LOGS shortcut
DEBUG = logger.debug
INFO = logger.info
WARNING = logger.warning
ERROR = logger.error
CRITICAL = logger.critical

# Takes number of errors
_TOTAL_ERRORS = 0


# ----------------------------------------------------------------------------------------------------------------------
# UTILITY METHODS
# ----------------------------------------------------------------------------------------------------------------------
# Printing Exception
def _print_exception(e):
    print("ERROR: Something went wrong -> %s " % e)


# Takes care of passing the text and the pattern to the stripper
def _refactor(str_md, pattern) -> str:
    outcome = replacer(str_md, pattern)

    return outcome


# Writing CSV
def _csv_writer(writer, row, destination, num_readme, lang,
                code='', en_percentage='', other_code='', other_percentage=''):

    # will return input as output
    destination = row['CloneDirectory'] if not _MOVE else destination

    DEBUG("Writing CSV...")
    writer.writerow({'Index': '%s' % row['Index'],
                     'CloneDirectory': '%s' % destination,
                     'ReadmeAnalyzed': '%s' % num_readme,
                     'Language': '%s' % lang,
                     'Code1': '%s' % code,
                     'Percentage1': '%s' % en_percentage,
                     'Code2': '%s' % other_code,
                     'Percentage2': '%s' % other_percentage
                     })


# Getting name of the repository
def _name_of(repo) -> str:

    return PATH.abspath(repo).split(PATH.sep)[-1]


# Getting absolute path of the Destination - if MOVE = True
def _get_destination(relative_destination, repository) -> str:

    return Path(PATH.abspath(relative_destination) + SEPARATOR + _name_of(repository))


# Checking if README.md exists in repository folder structure
def _find_all_md(repo) -> []:
    DEBUG("Looking for all readme...")
    files = [f for ext in _EXTENSIONS for f in gl(os.path.join(repo, '**', ext), recursive=True)]

    return [i for i in files if "readme" in PATH.basename(i.lower())]


# Transform percentage into ##.# format
def _format(percent) -> str:

    return "{:.1f}".format(100 * percent)


# Checking if README is valid
def _is_valid(string) -> bool:
    condition = re.match(_ILLEGAL_STRING, string)

    return False if len(string) < _MIN_LENGTH or condition else True


# Checking the typology of the approach and perform the detection
def _language_detector(target) -> []:
    dl.DetectorFactory.seed = _OUTPUT_TYPE

    return dl.detect_langs(target)


# Checking if if english was detected
def _is_there_english(percentage) -> bool:

    return True if percentage > 0.0 else False


# Analyzing json
def _analyze_results(results) -> Tuple[float, float]:
    en_base = 0.0
    other_base = 0.0

    # Scanning detections
    for detection in results["Detections"]:
        for value in detection["Detection"]:
            if value["code"] == "en":
                en_base += float(value["percentage"])
            else:
                other_base += float(value["percentage"])

    return en_base, other_base


# Transforming local percentage into global
def _parse_results(en, other) -> Tuple[float, float]:
    eng_percentage = float(en / (en + other))
    other_percentage = float(other / (en + other))

    return eng_percentage if en > 0.0 else 0, other_percentage if other > 0.0 else 0


# Getting information for Log summary
def _log_info() -> Tuple[float, str, str, str]:
    start_time = time.time()
    is_move = "Moving ACTIVATED." if _MOVE else "Moving DISABLED."
    is_csv = "CSV generation ACTIVATED." if _CSV_OUTPUT else "CSV generation DISABLED."
    is_deterministic = "MOD: Determinism approach." if _OUTPUT_TYPE == 0 else "MOD: Non determinism approach."

    return start_time, is_move, is_csv, is_deterministic


# Getting Error counter
def increment_error():
    global _TOTAL_ERRORS
    _TOTAL_ERRORS += 1


# Takes care of replacing the target
def replacer(txt, pattern) -> Optional[str]:
    file = txt
    match_pattern = re.findall(pattern, txt, re.MULTILINE | re.DOTALL)
    if match_pattern:
        new_file = re.sub(pattern, '', file, flags=re.S)

        return new_file
    else:
        return None


# Generating input.csv
def generate_input() -> int:
    # Getting all folders(repositories) in the path provided
    folders_paths = [f.path for f in os.scandir(_REPOSITORIES_PATH) if f.is_dir()]

    with open(_CSV, 'w', newline='') as csv_file:
        writer = csv.DictWriter(csv_file, fieldnames=_INPUT_FIELDNAMES)

        # Writing fields
        writer.writeheader()

        index = 0
        for folder_path in folders_paths:
            writer.writerow({'Index': '%s' % index, 'CloneDirectory': '%s' % folder_path})
            index += 1

    return len(folders_paths)


# ----------------------------------------------------------------------------------------------------------------------
# MAIN METHODS
# ----------------------------------------------------------------------------------------------------------------------
# Takes care of checking the text and replacing the targets
def replacer_operator(str_md) -> str:

    # Removing Markdown Code Snippets
    strip_from_cs = _refactor(str_md, _CODE_SNIPPETS)
    str_md = strip_from_cs if strip_from_cs is not None else str_md

    # Removing Table Markdown
    strip_from_tables = _refactor(str_md, _TABLES)
    str_md = strip_from_tables if strip_from_tables is not None else str_md

    # Removing Markdown Links
    strip_from_links = _refactor(str_md, _LINKS)
    str_md = strip_from_links if strip_from_links is not None else str_md

    # Removing Markdown Images
    strip_from_images = _refactor(str_md, _IMAGES)
    str_md = strip_from_images if strip_from_images is not None else str_md

    # Removing Markdown URLs
    strip_from_urls = _refactor(str_md, _URLS)
    str_md = strip_from_urls if strip_from_urls is not None else str_md

    # Removing Html Markdown
    strip_from_html = _refactor(str_md, _HTML)
    str_md = strip_from_html if strip_from_html is not None else str_md

    # Removing All Remaining Special Characters
    strip_from_all = _refactor(str_md, _REMAINING_SPECIAL_CHARS)
    str_md = strip_from_all if strip_from_all is not None else str_md

    # TODO: Add other patterns...

    return str_md


# Analyzes the result and decides the destination of the repositories
def operator(detections, repo, writer, row, num):
    try:
        # Getting results
        en_base, other_base = _analyze_results(detections)

        # Parsing results
        en_final, other_final = _parse_results(en_base, other_base)

        # Formatting percentage
        other = _format(other_final)
        en = _format(en_final)

        INFO("!! How much English is in repository? : {} %".format(en))

        # Checking if repository is english
        if _is_there_english(en_base) and en_final >= _EN_MAX:
            DEBUG("Repository is written in english.")

            # Writing CSV
            if _CSV_OUTPUT:
                oth = '' if en_final == 1.0 else 'others'
                oth_percentage = '' if en_final == 1.0 else other
                _csv_writer(writer, row, _get_destination(_ENGLISH, repo),
                            num, 'english', 'en', en, oth, oth_percentage)

            # Moving Repository
            if _MOVE:
                INFO("Moving repository to 'english' destination!")
                MOVE_TO(repo, "%s" % _ENGLISH)

        # Checking if repository is mixed
        if _is_there_english(en_base) > 0 and _EN_MIN < en_final < _EN_MAX:
            DEBUG("Repository is written in multiple languages.")

            # Writing CSV
            if _CSV_OUTPUT:
                _csv_writer(writer, row, _get_destination(_MIXED, repo),
                            num, 'mixed', 'en', en, 'others', other)

            # Moving Repository
            if _MOVE:
                INFO("Moving repository to 'mixed' destination!")
                MOVE_TO(repo, "%s" % _MIXED)

        # checking if repository is not in English
        if not _is_there_english(en_base) or en_final <= _EN_MIN:
            DEBUG("Repository is not written in english.")

            # Writing CSV
            if _CSV_OUTPUT:
                if en_final > 0.0:
                    _csv_writer(writer, row, _get_destination(_NOT_ENGLISH, repo),
                                num, 'not english', 'en', en, 'others', other)
                else:
                    _csv_writer(writer, row, _get_destination(_NOT_ENGLISH, repo),
                                num, 'not english', 'others', other)

            # Moving Repository
            if _MOVE:
                INFO("OPERATION: Moving repository to 'not english' destination!")
                MOVE_TO(repo, "%s" % _NOT_ENGLISH)

    # Catching exceptions
    except Exception as ex:
        increment_error()
        CRITICAL("Exception caught: ", exc_info=True)
        _print_exception(" Raised by inspector method on repository: %s - %s " % (_name_of(repo), ex))


# ----------------------------------------------------------------------------------------------------------------------
# MAIN
# ----------------------------------------------------------------------------------------------------------------------
def main():
    try:
        DEBUG("Opening input CSV file...")
        with open(_CSV, encoding='utf-8', errors='ignore') as srcfile:
            readers = csv.DictReader(srcfile, delimiter=',')

            DEBUG("Creating output CSV file...")
            with open(_NEW_CSV, 'w', newline='') as csv_file:
                writer = csv.DictWriter(csv_file, fieldnames=_FIELDNAMES)

                # Writing fields
                writer.writeheader()

                DEBUG("Scanning CSV rows...\n")
                for row in readers:

                    # Divisor
                    INFO("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ")

                    # Taking the repository path from CSV row
                    repo_src = row['CloneDirectory']

                    # Checking if the repository cannot be cloned
                    if 'null' in repo_src:
                        WARNING("Can't access because repository cannot be cloned! ")
                        _csv_writer(writer, row, 'null', '', '')
                        continue

                    INFO("Analyzing Repository: %s" % _name_of(repo_src).upper())
                    INFO("Repository src: %s" % repo_src)

                    # Checking if readme exist in directory and its subdirectories
                    readme_paths = _find_all_md(repo_src)

                    # Scanning files in repository
                    if readme_paths:
                        INFO("README found = %s" % len(readme_paths))

                        # defining the json for the results
                        detections = {
                            "Detections": []
                        }

                        # Index that counts the number of the readme being analyzed
                        counter = 0

                        # Scanning all readme in repository
                        for readme_path in readme_paths:
                            try:
                                # Increasing counter
                                counter += 1

                                log = "Opening Readme:" if len(readme_paths) == 1 \
                                    else "Opening Readme n.%s: ---" % counter
                                INFO("%s %s" % (log, readme_path))

                                with open(readme_path, 'r', encoding='utf-8', errors='ignore') as f:
                                    DEBUG("Cleaning...")
                                    str_md = replacer_operator(f.read())

                                # Doing checks after closing target
                                if str_md and not str_md.isspace() and _is_valid(str_md):
                                    DEBUG("Checking if readme is empty.. [ISN'T EMPTY]")

                                    try:
                                        DEBUG("Entering in the Language Detection phase...")
                                        results = _language_detector(str_md)

                                    # Catching exceptions
                                    except LangDetectException:
                                        increment_error()
                                        ERROR("Exception caught: ", exc_info=True)
                                        pass

                                    else:
                                        for result in results:
                                            INFO("LANGUAGE DETECTED: {} with {}% confidence."
                                                 .format(result.lang, _format(result.prob)))

                                            detections["Detections"].append({"Detection": [
                                                {
                                                    "code": result.lang,
                                                    "percentage": result.prob
                                                }
                                            ]})

                                else:
                                    WARNING("Checking if readme is empty.. [IS EMPTY]")

                            # Catching exceptions
                            except EnvironmentError:
                                increment_error()
                                ERROR("Exception caught: ", exc_info=True)
                                pass

                        # Checking for results
                        if len(detections['Detections']) > 0:
                            DEBUG("Entering in the Data Study phase...")
                            operator(detections, repo_src, writer, row, len(readme_paths))

                        else:
                            WARNING("No written readme found!")

                            # Writing CSV
                            if _CSV_OUTPUT:
                                _csv_writer(writer, row, _get_destination(_UNKNOWN, repo_src),
                                            len(readme_paths), 'unknown')

                            # Moving Repository
                            if _MOVE:
                                INFO("Moving repository to 'unknown' destination!")
                                MOVE_TO(repo_src, "%s" % _UNKNOWN)

                    else:
                        WARNING("README does not exist, or the format is not supported!")

                        # Writing CSV
                        if _CSV_OUTPUT:
                            _csv_writer(writer, row, _get_destination(_UNKNOWN, repo_src), 0, 'unknown')

                        # Moving Repository
                        if _MOVE:
                            INFO("Moving repository to 'unknown' destination!")
                            MOVE_TO(repo_src, "%s" % _UNKNOWN)

    # Catching exceptions
    except Exception as ex:
        increment_error()
        CRITICAL("Exception caught: ", exc_info=True)
        _print_exception(" Raised by main method on repository: %s - %s " % (_name_of(repo_src), ex))


# ----------------------------------------------------------------------------------------------------------------------
# START
# ----------------------------------------------------------------------------------------------------------------------
# Summary logs
_start_time, _is_move, _is_csv, _is_deterministic = _log_info()

print("START! \n\nLOADING...\n")
INFO("{} - STARTING SCRIPT..."
     "\n\nScript Config\n"
     "- - - - - - - - - - - - - - - - - - - -"
     "\n- {}\n- {}\n- {}\n"
     "- - - - - - - - - - - - - - - - - - - -"
     "\n\n-->\n".format(TODAY.strftime("%d/%m/%Y"), _is_move, _is_deterministic, _is_csv))

# Starting Detector Script
if __name__ == "__main__":  # So you can use [ language_detection.py ] as module for testing
    if _INPUT_GENERATOR == 1:

        # Generating first the input and next starting main
        DEBUG("Generating input.csv with path: %s \n" % _REPOSITORIES_PATH)
        repositories_found = generate_input()
        DEBUG("Repositories Found: %s" % repositories_found)
        main()

    else:
        main()

# Calculates the execution time of the script
_TOTAL_TIME = time.time() - _start_time
INFO("\n\nFinished in: {} seconds with {} Errors.".format(_TOTAL_TIME, _TOTAL_ERRORS))
print("FINISH!")
