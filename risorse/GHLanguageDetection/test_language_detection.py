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
import language_detection as dt
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

# Regex Tests
md_withTable = sysPath("repositories_test/README_files/README_table.md")
md_withLink = sysPath("repositories_test/README_files/README_link.md")
md_withImage = sysPath("repositories_test/README_files/README_image.md")
md_withCodeSnippet = sysPath("repositories_test/README_files/README_codeSnippet.md")
md_withCodeUrl = sysPath("repositories_test/README_files/README_url.md")
md_withHtml = sysPath("repositories_test/README_files/README_html.md")
md_withRemaining = sysPath("repositories_test/README_files/README_remaining.md")
md_all = sysPath("repositories_test/README_files/README_all.md")


# Test Methods   
# ----------------------------------------------------------------------------------------------------------------------
# Test if README is in directory
def test_exists():
    if dt._find_all_md(repo_withMd):
        assert True
    else:
        assert False


# Test if README is not in directory
def test_not_exists():
    if not dt._find_all_md(repo_withNoMd):
        assert True
    else:
        assert False


# Test if README is in directory and its subdirectories (nested case)
def test_exists_global():
    if dt._find_all_md(repo_withNestedMd):
        assert True
    else:
        assert False


# Test if the renamed README is in directory
def test_exists_renamed_md():
    if dt._find_all_md(repo_withRenamedMd):
        assert True
    else:
        assert False


# Test if README is empty
def test_empty_md():
    with open(PATH.abspath(dt._find_all_md(repo_withEmptyMd)[0]), 'r', encoding='utf8') as f:
        str_md = dt.strip_inspector("", f.read()).replace("\n", " ")
        if str_md and not str_md.isspace():
            assert True
        else:
            assert False


# Test if README does not empty
def test_full_md():
    with open(PATH.abspath(dt._find_all_md(repo_withFullMd)[0]), 'r', encoding='utf8') as f:
        str_md = dt.strip_inspector("", f.read()).replace("\n", " ")
        if str_md and not str_md.isspace():
            assert True
        else:
            assert False


# Test elimination of tables
def test_remove_table():
    with open(PATH.abspath(md_withTable), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor("", str_md, dt._TABLES)
        if output and not output.isspace():
            assert False
        else:
            assert True


# Test elimination of links
def test_remove_links():
    with open(PATH.abspath(md_withLink), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor("", str_md, dt._LINKS)
        if output and not output.isspace():
            assert False
        else:
            assert True


# Test elimination of URLs
def test_remove_urls():
    with open(PATH.abspath(md_withCodeUrl), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor("", str_md, dt._URLS)
        if output and not output.isspace():
            assert False
        else:
            assert True


# Test elimination of images
def test_remove_images():
    with open(PATH.abspath(md_withImage), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor("", str_md, dt._IMAGES)
        if output and not output.isspace():
            assert False
        else:
            assert True


# Test elimination of code snippet
def test_remove_code_snippet():
    with open(PATH.abspath(md_withCodeSnippet), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor("", str_md, dt._CODE_SNIPPETS)
        if output and not output.isspace():
            assert False
        else:
            assert True


# Test elimination of Html
def test_remove_html():
    with open(PATH.abspath(md_withHtml), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor("", str_md, dt._HTML)
        print(output)
        if output.strip() == 'The HTML - test regex.':
            assert True
        else:
            assert False


# Test elimination all Remaining Special Character
def test_remove_remaining():
    with open(PATH.abspath(md_withRemaining), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt._refactor("", str_md, dt._REMAINING_SPECIAL_CHARS)
        print(output)
        if output.strip() == 'TesTtEStTesT的模拟仿真程序':
            assert True
        else:
            assert False


# Test elimination of all markdowns
def test_all_markdown_removal():
    with open(PATH.abspath(md_all), 'r', encoding='utf8') as f:
        str_md = f.read()
        output = dt.strip_inspector("", str_md)
        print(output)
        if output.strip() == 'TESTTESTTEE':
            assert True
        else:
            assert False


# Test the correct transformation of the percentages
def test_percentage_format():
    percentage = 0.499928373951075347813
    assert dt._format(percentage) == str(50.0)


# Test if destination folders exists
@pytest.mark.xfail(reason="Folders must exists to pass the test!")  # COMMENT if folders exists
def test_folder_exists():
    if PATH.isdir(dt.DESTINATION_ENGLISH) and PATH.isdir(dt.DESTINATION_NOT_ENGLISH) \
            and PATH.isdir(dt.DESTINATION_MIXED) and PATH.isdir(dt.DESTINATION_UNKNOWN):
        assert True
    else:
        assert False


# Test if string contains only digits
def test_md_only_digits():
    if not dt._is_valid("12351236523875"):
        assert True
    else:
        assert False


# Test if string contains less than dt.MIN_LENGTH
def test_md_insufficient_text():
    if not dt._is_valid("less than %s " % dt._MIN_LENGTH):
        assert True
    else:
        assert False


# Test if string contains only special characters
def test_md_special_char():
    if not dt._is_valid("???_+><#$%@"):
        assert True
    else:
        assert False


# Test if correct execution of name of
def test_name_of():
    path = "/Users/user/Folder/file"
    name = dt._name_of(path)
    if name == 'file':
        assert True
    else:
        assert False


# Test if there is not english
def test_is_there_english():
    percentage_of_english = 0.0
    if not dt._is_there_english(percentage_of_english):
        assert True
    else:
        assert False


# Test if percentage are properly formatted
def test_percentage_parser():

    en = 100.0
    fr = 80.0
    cn = 20.0
    ge = 30.0
    ca = 70.0
    it = 100.0

    total_other = cn + ge + ca + it + fr
    eng_percentage, other_percentage = dt._parse_results(en, total_other)

    if eng_percentage == 0.25 and other_percentage == 0.75:
        assert True
    else:
        assert False
