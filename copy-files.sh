#!/bin/bash

# Check if the source and target directories are provided as parameters
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <source_directory> <target_directory>"
    exit 1
fi

# Function to recursively overwrite files
overwrite_files() {
    local source_dir=$1
    local target_dir=$2

    # Iterate over the files in the source directory
    for source_file in "$source_dir"/*; do
        # Check if the current item is a file
        if [ -f "$source_file" ]; then
            # Get the filename without the path
            filename=$(basename "$source_file")

            # Check if the corresponding file exists in the target directory
            target_file="$target_dir/$filename"
            if [ -f "$target_file" ]; then
                # Overwrite the file in the target directory
                cp "$source_file" "$target_file"
                echo "Overwritten $filename"
            else
                echo "File $filename does not exist in the target directory"
            fi
        elif [ -d "$source_file" ]; then
            # Recursively overwrite files in subdirectories
            subfolder_name=$(basename "$source_file")
            mkdir -p "$target_dir/$subfolder_name"
            overwrite_files "$source_file" "$target_dir/$subfolder_name"
        fi
    done
}

# Assign the source and target directories to variables
source_dir="$1"
target_dir="$2"

# Call the function with the specified source and target directories
overwrite_files "$source_dir" "$target_dir"

