#!/bin/bash
find . ! \( -name "*.pid" -o -name "*.log" -o -name "ENV" \) -type f -delete
