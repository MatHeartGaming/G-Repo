"""
TEST LANGUAGE DETECTOR SCRIPT
Author: Anas Mounsif - Università Degli Studi Della Basilicata

Requirements:
Python 3.8
PyTest

Usage:
RECOMMENDED: [ pytest ] in test_detector folder
Or [ py.test -k <method_name> -v ] for testing specific method.
For more information about PyTest visit: https://docs.pytest.org/en/stable/contents.html
"""

# IMPORTS
# ----------------------------------------------------------------------------------------------------------------------
# Importing Libraries
import pytest
from pathlib import Path as sysPath
import detector as dt
import os

# Config shortcut
PATH = os.path

# Directories and files for Testing
repo_withMd = sysPath("repositories_test/repo_withReadme/")
repo_withNoMd = sysPath("repositories_test/repo_withNoReadme/")
repo_withNestedMd = sysPath("repositories_test/repo_withNestedReadme/")
repo_withEmptyMd = sysPath("repositories_test/repo_withEmptyReadme/")
repo_withFullMd = sysPath("repositories_test/repo_withFullReadme/")
repo_withRenamedMd = sysPath("repositories_test/repo_withRenamedReadme/")

md_withTable = sysPath("repositories_test/README_files/README_table.md")
md_withLink = sysPath("repositories_test/README_files/README_link.md")
md_withImage = sysPath("repositories_test/README_files/README_image.md")
md_withCodeSnippet = sysPath("repositories_test/README_files/README_codeSnippet.md")


# Test Methods
# ----------------------------------------------------------------------------------------------------------------------
# Test if README is in directory
def test_exists():
    if dt.exists(repo_withMd):
        assert True
    else:
        assert False


# Test if README is not in directory
def test_not_exists():
    if not dt.exists(repo_withNoMd):
        assert True
    else:
        assert False


# Test if README is in directory and its subdirectories (nested case)
def test_exists_global():
    if dt.exists(repo_withNestedMd):
        assert True
    else:
        assert False


# Test if the renamed README is in directory
def test_exists_renamed_md():
    if dt.exists(repo_withRenamedMd):
        assert True
    else:
        assert False


# Test if README is empty
def test_empty_md():
    with open(PATH.abspath(dt.exists(repo_withEmptyMd)[0]), 'r', encoding='utf8') as f:
        str_md = dt.strip_inspector("", f.read()).replace("\n", " ")
        if str_md and not str_md.isspace():
            assert False
        else:
            assert True


# Test if README does not empty
def test_full_md():
    with open(PATH.abspath(dt.exists(repo_withFullMd)[0]), 'r', encoding='utf8') as f:
        str_md = dt.strip_inspector("", f.read()).replace("\n", " ")
        if str_md and not str_md.isspace():
            assert True
        else:
            assert False


# Test elimination of tables
def test_remove_table():
    with open(PATH.abspath(md_withTable), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt.refactor("", str_md, dt.TABLES)
        if output and not output.isspace():
            assert False
        else:
            assert True


# Test elimination of links
def test_remove_links():
    with open(PATH.abspath(md_withLink), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt.refactor("", str_md, dt.LINKS)
        if output and not output.isspace():
            assert False
        else:
            assert True


# Test elimination of images
def test_remove_images():
    with open(PATH.abspath(md_withImage), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt.refactor("", str_md, dt.IMAGES)
        if output and not output.isspace():
            assert False
        else:
            assert True


# Test elimination of code snippet
def test_remove_code_snippet():
    with open(PATH.abspath(md_withCodeSnippet), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt.refactor("", str_md, dt.CODE_SNIPPETS)
        print(output)
        if output and not output.isspace():
            assert False
        else:
            assert True


# Test the correct transformation of the percentages
def test_percentage_format():
    percentage = 0.499928373951075347813
    assert dt.format_percentage(percentage) == str(50.0)


# Test if destination folders exists - Folders must exists to pass the test!
def test_folder_exists():
    if PATH.isdir(dt.DESTINATION_ENGLISH) and PATH.isdir(dt.DESTINATION_NOT_ENGLISH) \
            and PATH.isdir(dt.DESTINATION_MIXED) and PATH.isdir(dt.DESTINATION_UNKNOWN):
        assert True
    else:
        assert False


# Test if string contains only digits
def test_md_only_digits():
    if not dt.is_valid("12351236523875"):
        assert True
    else:
        assert False


# Test if string contains less than dt.MIN_LENGTH
def test_md_insufficient_text():
    if not dt.is_valid("less than 15"):
        assert True
    else:
        assert False


# Test if string contains only special characters
def test_md_special_char():
    if not dt.is_valid("???_+><#$%@"):
        assert True
    else:
        assert False
