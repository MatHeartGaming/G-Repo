"""
LANGUAGE DETECTOR SCRIPT
Author: Anas Mounsif - Università Degli Studi Della Basilicata

Requirements:
six
Python 3.8
Probably glob

Usage:
[ python3 detector.py ] or [ python3 detector.py -h ] for more information.
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

# IF THE SCRIPT IS USED INDIVIDUALLY
# ----------------------------------------------------------------------------------------------------------------------
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

# PREFERENCES
# ----------------------------------------------------------------------------------------------------------------------
# Set to 0 for deterministic result, 1 for non-deterministic
OUTPUT_TYPE = 1

# True if you want the script to move the repositories else False - attention to ACCESS DENIED problem!
MOVE = False

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
NULL = 'null'
MIN_LENGTH = 50

# Regex patterns
TABLES = r"^(\|[^\n]+\|\r?\n)((?:\|:?[-]+:?)+\|)(\n(?:\|[^\n]+\|\r?\n?)*)?$"
URLS = r"https?:\/\/?[\da-z\.-]+\.[a-z\.]{2,6}[\/\w \.-]*"
IMAGES = r"!\[[^\]]+\]\([^)]+\)"
CODE_SNIPPETS = r"(```.+?```)"
LINKS = r"\[.*?\]\(.*?\)"
HTML = r"\<.*?\>"
ILLEGAL_STRING = r'^[_\W0-9]+$'


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
# Printing Exception
def print_exception(e):
    print("ERROR: Something went wrong -> %s " % e)


# Takes care of passing the text and the pattern to the stripper
def refactor(repository, str_md, pattern):
    outcome = stripper(repository, str_md, pattern)
    return outcome


# Writing CSV
def csv_writer(writer, row, destination, language, lang_code, lang_percentage, optional_code, optional_percentage):

    # LOG
    LOG("Writing CSV file! \n")

    if not MOVE:
        destination = row['CloneDirectory']

    writer.writerow({'Index': '%s' % row['Index'], 'CloneDirectory': '%s' % destination, 'Language': '%s' % language,
                     'Code1': '%s' % lang_code, 'Percentage1': '%s' % lang_percentage, 'Code2': '%s' % optional_code,
                     'Percentage2': '%s' % optional_percentage})


# Getting absolute path of the target
def get_abspath(target):
    return PATH.abspath(target)


# Getting absolute path of the repository
def get_name(repo):
    return get_abspath(repo).split(PATH.sep)[-1]


# Getting absolute path of the Destination - if MOVE = True
def get_destination(destination, repository) -> str:
    return sysPath(get_abspath(destination) + SEPARATOR + get_name(repository))


# Checking if README.md exists in repository folder structure
def exists(repository_dir):
    paths = []
    for ext in ('*.md', '*.MD', '*.markdown', '*.MARKDOWN'):
        # Passing "readme" and not variable README for correct execution of tests
        p = [i for i in gl(PATH.join(repository_dir, '**', ext), recursive=True) if "readme" in PATH.basename(i.lower())]
        paths.extend(p)
    return paths


# Transform percentage to ##.#
def format_percentage(percent):
    return "{:.1f}".format(100*percent)


# Checking if README.md is valid
def is_valid(string):
    if len(string) < MIN_LENGTH or regex.match(ILLEGAL_STRING, string):
        return False
    else:
        return True


# MAIN METHODS
# ----------------------------------------------------------------------------------------------------------------------
# Takes care of replacing the target
def stripper(repository, txt, pattern):
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
        # LOG
        LOG("Error: %s" % ex)
        print_exception(" Catched by stripper method on repository: {} - {} ".format(get_name(repository), ex))
        # pass


# Takes care of checking the text and replacing the targets
def strip_inspector(repository, str_md):

    # Removing Markdown Code Snippets
    str_from_cs = refactor(repository, str_md, CODE_SNIPPETS)
    str_md = str_from_cs if str_from_cs is not None else str_md

    # Removing Table Markdown
    str_from_tables = refactor(repository, str_md, TABLES)
    str_md = str_from_tables if str_from_tables is not None else str_md

    # Removing Markdown Links
    str_from_links = refactor(repository, str_md, LINKS)
    str_md = str_from_links if str_from_links is not None else str_md

    # Removing Markdown Images
    str_from_images = refactor(repository, str_md, IMAGES)
    str_md = str_from_images if str_from_images is not None else str_md

    # Removing Markdown URLs
    str_from_urls = refactor(repository, str_md, URLS)
    str_md = str_from_urls if str_from_urls is not None else str_md

    # TODO -> add other patterns...

    return str_md


# Check the typology of the result and perform the detection
def detector(target):
    dl.DetectorFactory.seed = OUTPUT_TYPE
    return dl.detect_langs(target)


# Analyzes the result and decides the destination of the repositories
def inspector(list_of_results, repo, writer, row):
    try:
        # LOGS
        LOG("---------------------------------------------------")
        LOG("Analyzing Repository: %s" % get_name(repo))
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
            LOG("Language Detected: {} with {}% confidence.".format(result.lang, format_percentage(result.prob)))

        # Checking if README is in English
        if any(result.lang == "en" and result.prob >= 0.90 for result in list_of_results):

            # Update CSV
            csv_writer(writer, row, get_destination(DESTINATION_ENGLISH, repo), 'english', first_code, first_percentage,
                       second_code, second_percentage)
            if MOVE:
                # LOG
                LOG("OPERATION: Moving repository to 'english' folder because README is written in english!")

                # Moving repository in "english" folder
                MOVE_TO(repo, "%s" % DESTINATION_ENGLISH)

        # checking if README is not in English
        if any(result.lang == "en" and result.prob <= 0.10 for result in list_of_results) \
                or not any(result.lang == "en" for result in list_of_results):

            # Update CSV
            csv_writer(writer, row, get_destination(DESTINATION_NOT_ENGLISH, repo), 'not english', first_code,
                       first_percentage, second_code, second_percentage)
            if MOVE:
                # LOG
                LOG("OPERATION: Moving repository to 'not english' folder because README isn't written in english!")

                # Moving repository in "not_english" folder
                MOVE_TO(repo, "%s" % DESTINATION_NOT_ENGLISH)

        # Checking if README is mixed
        if any(result.lang == "en" and 0.10 < result.prob < 0.90 for result in list_of_results):

            # Update CSV
            csv_writer(writer, row, get_destination(DESTINATION_MIXED, repo), 'mixed', first_code, first_percentage,
                       second_code, second_percentage)
            if MOVE:
                # LOG
                LOG("OPERATION: Moving repository to 'mixed' folder because README is written in different languages!")

                # Moving repository in "mixed" folder
                MOVE_TO(repo, "%s" % DESTINATION_MIXED)

    # Catching exceptions
    except Exception as ex:
        # LOG
        LOG("Error: %s" % ex)
        print_exception(" Catched by inspector method on repository: {} - {} ".format(get_name(repo), ex))
        # pass


# FIRST METHOD
# ----------------------------------------------------------------------------------------------------------------------
def main():
    try:
        # Opening CSV
        with open(CSV, encoding='utf-8', errors='ignore') as csvfile:
            readcsv = csv.DictReader(csvfile, delimiter=',')
            # Writing new CSV
            with open(NEW_CSV, 'w', newline='') as csv_file:
                writer = csv.DictWriter(csv_file, fieldnames=FIELDNAMES)
                # Writing fields
                writer.writeheader()
                # Scanning rows in CSV
                for row in readcsv:
                    # Taking the path from CSV
                    repository_dir = row['CloneDirectory']

                    # Checking if the repository cannot be cloned
                    if NULL in repository_dir:
                        # LOG
                        LOG("Can't access because repository cannot be cloned! ")

                        csv_writer(writer, row, NULL, '', '', '', '', '')
                        continue

                    # Check if readme exist in directory and its sub directories
                    file_found = exists(repository_dir)
                    # Scanning files in repository
                    if file_found and (file for file in file_found if README in file.lower()):
                        # Opening Target - getting always the first README
                        with open(file_found[0], 'r', encoding='utf-8', errors='ignore') as f:
                            # Cleaning
                            str_md = strip_inspector(get_name(repository_dir), f.read())

                        # Doing stuff after closing target
                        if str_md and not str_md.isspace() and is_valid(str_md):
                            # Managing the result of the language detector
                            results = detector(str_md)
                            inspector(results, repository_dir, writer, row)
                        else:
                            # LOG
                            LOG("---------------------------------------------------")
                            LOG("Analyzing Repository: %s" % get_name(repository_dir))
                            LOG("README is empty!")

                            # Update CSV
                            csv_writer(writer, row, get_destination(DESTINATION_UNKNOWN, repository_dir),
                                           'unknown', '', '', '', '')

                            if MOVE:
                                # LOG
                                LOG("Moving repository to 'unknown' folder!")

                                # Moving repository in unknown folder because README is empty
                                MOVE_TO(repository_dir, "%s" % DESTINATION_UNKNOWN)
                    else:
                        # LOGS
                        LOG("---------------------------------------------------")
                        LOG("Analyzing Repository: %s" % get_name(repository_dir))
                        LOG("README does not exist!")

                        # Update CSV
                        csv_writer(writer, row, get_destination(DESTINATION_UNKNOWN, repository_dir),
                                       'unknown', '', '', '', '')

                        if MOVE:
                            # LOG
                            LOG("Moving repository to 'unknown' folder!")

                            # Moving repository in unknown folder because README does not exist
                            MOVE_TO(repository_dir, "%s" % DESTINATION_UNKNOWN)

    # Catching exceptions
    except LangDetectException:
        # LOG
        message = "Passing to Language Detector empty string, probably not null, but without characters!"
        LOG("Error: %s " % message)
        print("Error on repository: {}, {}".format(get_name(repository_dir), message))

    except Exception as ex:
        # LOG
        LOG("Error: %s" % ex)
        print_exception(" Catched by main method on repository: {} - {} ".format(get_name(repository_dir), ex))


# START
# ----------------------------------------------------------------------------------------------------------------------
activation_move = "MOVE activated." if MOVE else "MOVE disabled."
activation_deterministic = "Deterministic algorithm activated." if OUTPUT_TYPE == 0 else "Non Deterministic algorithm" \
                                                                                         " activated."
# LOGS
LOG("%s - Starting script...\n" % TODAY.strftime("%d/%m/%Y"))
LOG("\n\n- - - - - - - - - - - - - - - - - - -\nScript Config: \n - {}\n - {}\n- - - - - - - - - - - - - - - - - - -\n\n"
    .format(activation_move, activation_deterministic))

# Starting Detector Script
main()

# LOG
LOG("Finish!")
