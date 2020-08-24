"""
TEST LANGUAGE DETECTOR SCRIPT
Author: Anas Mounsif - Università Degli Studi Della Basilicata

Requirements:
Python 3.8
PyTest

Usage:
RECOMMENDED: [ pytest -v ] in test_detector folder
Or [ py.test -k <method_name> -v ] for testing specific method.
For more information about PyTest visit: https://docs.pytest.org/en/stable/contents.html
"""

# IMPORTS
# ----------------------------------------------------------------------------------------------------------------------
# Importing Libraries
import language_detection as dt
import pytest
from pathlib import Path as sysPath
import os

# Directories and files for Testing
repo_withMd = sysPath("test-stuff/repo-withReadme/")
repo_withNoMd = sysPath("test-stuff/repo-withNoReadme/")
repo_withNestedMd = sysPath("test-stuff/repo-withNestedReadme/")
repo_withRenamedMd = sysPath("test-stuff/repo-withRenamedReadme/")
repo_withEmptyMd = sysPath("test-stuff/repo-withEmptyReadme/")
repo_withFullMd = sysPath("test-stuff/repo-withFullReadme/")

# Regex Tests
md_withTable = sysPath("test-stuff/readme-tests/README-table.md")
md_withLink = sysPath("test-stuff/readme-tests/README-link.md")
md_withImage = sysPath("test-stuff/readme-tests/README-image.md")
md_withCodeSnippet = sysPath("test-stuff/readme-tests/README-codeSnippet.md")
md_withCodeUrl = sysPath("test-stuff/readme-tests/README-url.md")
md_withHtml = sysPath("test-stuff/readme-tests/README-html.md")
md_withRemaining = sysPath("test-stuff/readme-tests/README-remaining.md")
md_all = sysPath("test-stuff/readme-tests/README-all.md")

# Config shortcut
PATH = os.path

_DETECTIONS = {
    "Detections": [{
        "Detection": [
            {"code": "en", "percentage": 0.4}, {"code": "en", "percentage": 0.3},
            {"code": "cn", "percentage": 0.7}, {"code": "it", "percentage": 0.6},
            {"code": "cn", "percentage": 1.0}, {"code": "en", "percentage": 1.0}
        ]
    }]
}


# ----------------------------------------------------------------------------------------------------------------------
# Test Methods
# ----------------------------------------------------------------------------------------------------------------------
# Testing Exists method
def test_exists():
    there_is_md = dt._find_all_md(repo_withMd)
    there_no_md = dt._find_all_md(repo_withNoMd)
    nested_md = dt._find_all_md(repo_withNestedMd)
    renamed_md = dt._find_all_md(repo_withRenamedMd)
    assert there_is_md
    assert not there_no_md
    assert nested_md
    assert renamed_md


# Testing if readme is empty and not
def test_empty_full():
    with open(PATH.abspath(dt._find_all_md(repo_withEmptyMd)[0]), 'r', encoding='utf8') as f:
        test_string_1 = dt.replacer_operator(f.read()).replace("\n", " ")
    with open(PATH.abspath(dt._find_all_md(repo_withFullMd)[0]), 'r', encoding='utf8') as f:
        test_string_2 = dt.replacer_operator(f.read()).replace("\n", " ")
    assert test_string_1
    assert not test_string_1.isspace()
    assert test_string_2
    assert not test_string_2.isspace()


# Testing elimination of tables
def test_remove_tables():
    with open(PATH.abspath(md_withTable), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor(str_md, dt._TABLES)
    assert output is not None
    assert not output.isspace()


# Testing elimination of links
def test_remove_links():
    with open(PATH.abspath(md_withLink), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor(str_md, dt._LINKS)
    assert output is not None
    assert output.isspace()


# Testing elimination of URLs
def test_remove_urls():
    with open(PATH.abspath(md_withCodeUrl), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor(str_md, dt._URLS)
    assert output == "<>\n"


# Testing elimination of images
def test_remove_images():
    with open(PATH.abspath(md_withImage), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor(str_md, dt._IMAGES)
    assert output is not None
    assert output.isspace()


# Testing elimination of code snippet
def test_remove_code_snippet():
    with open(PATH.abspath(md_withCodeSnippet), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor(str_md, dt._CODE_SNIPPETS)
    assert output is not None
    assert output.isspace()


# Testing elimination of Html
def test_remove_html():
    with open(PATH.abspath(md_withHtml), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor(str_md, dt._HTML)
    assert output.strip() == 'The HTML - test regex.'


# Testing elimination all Remaining Special Character
def test_remove_remaining():
    with open(PATH.abspath(md_withRemaining), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor(str_md, dt._REMAINING_SPECIAL_CHARS)
    assert output.strip() == 'TesTtEStTesT的模拟仿真程序'


# Testing elimination of all markdowns
def test_all_markdown_removal():
    with open(PATH.abspath(md_all), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt.replacer_operator(str_md)
    assert output.strip() == 'TESTTESTTEE'


# Testing the correct transformation of the percentages
def test_format():
    percentage = 0.49993589
    assert dt._format(percentage) == str(50.0)


# Testing if destination folders exists
@pytest.mark.xfail(reason="Folders must exists to pass the test!")  # COMMENT if folders exists
def test_isdir():
    assert PATH.isdir(dt._ENGLISH)
    assert PATH.isdir(dt._NOT_ENGLISH)
    assert PATH.isdir(dt._MIXED)
    assert PATH.isdir(dt._UNKNOWN)


# Testing if string contains is valid method
def test_is_valid():
    digit_condition = dt._is_valid("12351236523875")
    min_length_condition = dt._is_valid("less than %s " % dt._MIN_LENGTH)
    special_char_condition = dt._is_valid("???_+><#$%@")
    assert not digit_condition
    assert not min_length_condition
    assert not special_char_condition


# Testing if correct execution of name of
def test_name_of():
    path = "/root/user/folder/nested_folder"
    name = dt._name_of(path)
    assert name == 'nested_folder'


# Testing if there is not english
def test_is_there_english():
    percentage_of_english = 0.0
    assert not dt._is_there_english(percentage_of_english)


# Testing if percentage are properly formatted
def test_percentage_parser():
    en = 100.0
    other = 300.0
    eng_percentage, other_percentage = dt._parse_results(en, other)
    assert eng_percentage == 0.25
    assert other_percentage == 0.75


# Testing analyze results
def test_analyze_results():
    en_base, other_base = dt._analyze_results(_DETECTIONS)
    assert en_base == 1.7
    assert other_base == 2.3
