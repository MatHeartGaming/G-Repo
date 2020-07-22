"""
LANGUAGE DETECTOR SCRIPT
Author: Anas Mounsif - Università Degli Studi Della Basilicata

Requirements:
six
Python 3.8
Probably glob

Usage:
[ python3 detector.py ] or [ python3 detector.py -h] for more information.
"""

# IMPORTS
# ----------------------------------------------------------------------------------------------------------------------
# Importing Libraries
from pathlib import Path as sysPath
from glob import glob as gl
from datetime import date
import langdetect as dl
import shutil as sh
import re as regex
import argparse
import logging
import csv
import os

# Importing Exceptions from Libraries
from langdetect.lang_detect_exception import LangDetectException


# PREFERENCES
# ----------------------------------------------------------------------------------------------------------------------
# Set to 0 for deterministic result, 1 for non-deterministic
OUTPUT_TYPE = 1

# Colors Configuration
RED = '\033[91m'
END = '\033[0m'

# Defining the script description
description = ' This script is able to filter repositories based on the language used for writing the README. ' \
              '\n Use the normal language translation library found at this address: ' \
              '{}https://github.com/Mimino666/langdetect{}' \
              '\n And implements regular expressions for cleaning the files to be translated from the markdowns' \
              '\n At the end it generates a CSV file with all the information inside.'.format(RED, END)

# Initiating the parser with a description
parser = argparse.ArgumentParser(description=description, formatter_class=argparse.RawDescriptionHelpFormatter)

# Adding optional argument
parser.add_argument("-v", "--version", help=" show script version", action='version', version=' version: 3.0')
text = " {}Anas Mounsif{} - Università Degli Studi Della Basilicata.".format(RED, END)
parser.add_argument("-a", "--author", help=" show author information", action='version', version=text)
parser.parse_args()

# Modify as needed but pay attention to folder's structure
DESTINATION_ENGLISH = sysPath("../lingua/english/")
DESTINATION_NOT_ENGLISH = sysPath("../lingua/not_english/")
DESTINATION_MIXED = sysPath("../lingua/mixed/")
DESTINATION_UNKNOWN = sysPath("../lingua/unknown/")

# CSV config params
CSV = "input.csv"
NEW_CSV = "output.csv"
FIELDNAMES = ['Index', 'CloneDirectory', 'Language', 'Code1', 'Percentage1', 'Code2', 'Percentage2']
README = "readme.md"

# Regex patterns
TABLES = "^(\|[^\n]+\|\r?\n)((?:\|:?[-]+:?)+\|)(\n(?:\|[^\n]+\|\r?\n?)*)?$"
URLS = "https?:\/\/?[\da-z\.-]+\.[a-z\.]{2,6}[\/\w \.-]*"
IMAGES = "!\[[^\]]+\]\([^)]+\)"
CODE_SNIPPETS = "(```.+?```)"
LINKS = "\[.*?\]\(.*?\)"
HTML = "\<.*?\>"

# Config LOGS
logging.basicConfig(filename="log", filemode='w', format='%(asctime)s - %(message)s',
                    datefmt='%H:%M:%S', level=logging.DEBUG)

# Config shortcut
MOVE_TO = sh.move
PATH = os.path
SEPARATOR = os.sep
LOG = logging.info
TODAY = date.today()


# UTILITY METHODS
# ----------------------------------------------------------------------------------------------------------------------
def print_exception(e):
    print("ERROR: Something went wrong -> %s " % e)


def refactor(str_md, pattern):
    outcome = stripper(str_md, pattern)
    return outcome


def csv_writer(writer, row, destination, language, lang_code, lang_percentage, optional_code, optional_percentage):

    # LOGS
    LOG("Writing CSV file! \n")

    writer.writerow({'Index': '%s' % row['Index'], 'CloneDirectory': '%s' % destination, 'Language': '%s' % language,
                     'Code1': '%s' % lang_code, 'Percentage1': '%s' % lang_percentage, 'Code2': '%s' % optional_code,
                     'Percentage2': '%s' % optional_percentage})


def get_abspath(target):
    return PATH.abspath(target)


def get_name(repo):
    return get_abspath(repo).split(PATH.sep)[-1]


def get_destination(destination, repository) -> str:
    return sysPath(get_abspath(destination) + SEPARATOR + get_name(repository))


def exists(repository_dir):
    paths = gl(f'{repository_dir}/**/*[{README}]', recursive=True)
    path = [val for val in paths if not PATH.isdir(val)]
    return path


def format_percentage(percent):
    return "{:.1f}".format(100*percent)


# MAIN METHODS
# ----------------------------------------------------------------------------------------------------------------------
# Takes care of replacing the target
def stripper(txt, pattern):
    file = txt
    try:
        match_pattern = regex.findall(pattern, txt, regex.MULTILINE | regex.DOTALL)
        if match_pattern:
            new_file = regex.sub(pattern, '', file, flags=regex.S)
            return new_file
        else:
            return None

    # Catching exceptions
    except Exception as ex:
        print_exception(" Catched by stripper method - %s " % ex)


# Takes care of checking the text and replacing the targets
def strip_inspector(str_md):

    # Removing Markdown Code Snippets
    str_from_cs = refactor(str_md, CODE_SNIPPETS)
    str_md = str_from_cs if str_from_cs is not None else str_md

    # Removing Table Markdown
    str_from_tables = refactor(str_md, TABLES)
    str_md = str_from_tables if str_from_tables is not None else str_md

    # Removing Markdown Links
    str_from_links = refactor(str_md, LINKS)
    str_md = str_from_links if str_from_links is not None else str_md

    # Removing Markdown Images
    str_from_images = refactor(str_md, IMAGES)
    str_md = str_from_images if str_from_images is not None else str_md

    # TODO -> add other patterns...

    return str_md


# Check the typology of the result and perform the detection
def detector(target):
    dl.DetectorFactory.seed = OUTPUT_TYPE
    return dl.detect_langs(target)


# Analyzes the result and decides the destination of the repositories
def inspector(list_of_results, repository, writer, row):
    try:
        # LOGS
        LOG("Analyzing Repository: %s" % get_name(repository).upper())
        if len(list_of_results) > 1:
            LOG("* MULTIPLE LANGUAGES WERE DETECTED *")

        # Getting only first 2 results
        first_code = list_of_results[0].lang
        first_percentage = "%s" % format_percentage(list_of_results[0].prob)
        # Optional
        second_code = list_of_results[1].lang if len(list_of_results) > 1 else ''
        second_percentage = "%s" % format_percentage(list_of_results[1].prob) if len(list_of_results) > 1 else ''

        # LOGS
        for result in list_of_results:
            LOG("Language Detected: {} with confidence: {}%".format(result.lang, format_percentage(result.prob)))

        # Checking if README is in English
        if any(result.lang == "en" and result.prob >= 0.90 for result in list_of_results):

            # LOGS
            LOG("OPERATION: Moving repository to 'english' folder because README is written in english!\n")

            # Moving repository in "english" folder
            MOVE_TO(repository, "%s" % DESTINATION_ENGLISH)
            # Update CSV
            csv_writer(writer, row, get_destination(DESTINATION_ENGLISH, repository), 'english',
                       first_code, first_percentage, second_code, second_percentage)

        # checking if README is not in English
        if any(result.lang == "en" and result.prob <= 0.10 for result in list_of_results) \
                or not any(result.lang == "en" for result in list_of_results):

            # LOGS
            LOG("OPERATION: Moving repository to 'not english' folder because README is written in english!\n")

            # Moving repository in "not_english" folder
            MOVE_TO(repository, "%s" % DESTINATION_NOT_ENGLISH)
            # Update CSV
            csv_writer(writer, row, get_destination(DESTINATION_NOT_ENGLISH, repository), 'not english',
                       first_code, first_percentage, second_code, second_percentage)

        # Checking if README is mixed
        if any(result.lang == "en" and 0.10 < result.prob < 0.90 for result in list_of_results):

            # LOGS
            LOG("OPERATION: Moving repository to 'mixed' folder because README is written in english!\n")

            # Moving repository in "mixed" folder
            MOVE_TO(repository, "%s" % DESTINATION_MIXED)
            # Update CSV
            csv_writer(writer, row, get_destination(DESTINATION_MIXED, repository), 'mixed',
                       first_code, first_percentage, second_code, second_percentage)

    # Catching exceptions
    except Exception as ex:
        print_exception(" Catched by inspector method - %s " % ex)


# FIRST METHOD
# ----------------------------------------------------------------------------------------------------------------------
def main():
    try:
        # Opening CSV
        with open(CSV, encoding='utf-8') as csvfile:
            readcsv = csv.DictReader(csvfile, delimiter=',')
            # Writing new CSV
            with open(NEW_CSV, 'w', newline='') as csv_file:
                writer = csv.DictWriter(csv_file, fieldnames=FIELDNAMES)
                # Writing fields
                writer.writeheader()
                # Scanning rows in CSV
                for row in readcsv:
                    repository_dir = row['CloneDirectory']
                    # Scanning repository
                    for root_dir_path, _, files in os.walk(repository_dir):
                        # Check if readme exist in directory and its sub directories
                        file_found = exists(repository_dir)
                        # Scanning files in repository
                        if file_found and (file for file in files if README in file.lower()):
                            # Opening Target - getting always the first README
                            with open(get_abspath(file_found[0]), 'r', encoding='utf8') as f:
                                # Cleaning
                                str_md = strip_inspector(f.read())
                            # Doing stuff after closing target
                            if str_md and not str_md.isspace():
                                # Managing the result of the language detector
                                results = detector(str_md)
                                inspector(results, repository_dir, writer, row)
                            else:
                                # LOGS
                                LOG("Analyzing Repository: %s" % get_name(repository_dir))
                                LOG("Moving repository to 'unknown' folder because README is empty!\n")

                                # Moving repository in unknown folder because README is empty
                                MOVE_TO(repository_dir, "%s" % DESTINATION_UNKNOWN)
                                # Update CSV
                                csv_writer(writer, row, get_destination(DESTINATION_UNKNOWN, repository_dir),
                                           'unknown', '', '', '', '')
                        else:
                            # LOGS
                            LOG("Analyzing Repository: %s" % get_name(repository_dir))
                            LOG("Moving repository to 'unknown' folder because README does not exist!\n")

                            # Moving repository in unknown folder because README does not exist
                            MOVE_TO(root_dir_path, "%s" % DESTINATION_UNKNOWN)
                            # Update CSV
                            csv_writer(writer, row, get_destination(DESTINATION_UNKNOWN, repository_dir),
                                       'unknown', '', '', '', '')

    # Catching exceptions
    except LangDetectException:
        print("ERROR: Passing to Language Detector empty string, probably not null, but without characters! ")

    except Exception as ex:
        print_exception(" Catched by main method - %s " % ex)


# START
# ----------------------------------------------------------------------------------------------------------------------
# Starting Detector Script
LOG("%s - Starting script...\n" % TODAY.strftime("%d/%m/%Y"))

main()

LOG("Finish!")
