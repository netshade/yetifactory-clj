#!/bin/bash
cd /app
find . ! \( -name "*.pid" -o -name "*.log" -o -name "ENV" \) -type f -delete
