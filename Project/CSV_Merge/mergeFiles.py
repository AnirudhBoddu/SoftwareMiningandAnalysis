import pandas as pd
import os

# define the folder where the CSV files are stored
folder = "D:\\SoftwareMiningandAnalysis\\Project\\Metrics"

# create an empty dataframe to hold the merged data
merged_df = None

# iterate through all the files in the folder
for file_name in os.listdir(folder):
    if file_name.endswith(".csv"):
        file_path = os.path.join(folder, file_name)

        # read the CSV file into a dataframe
        df = pd.read_csv(file_path)

        # merge the dataframe with the merged dataframe on the "Filename" column
        if merged_df is None:
            merged_df = df
        else:
            merged_df = pd.merge(merged_df, df, on="Filename", how="inner")

if merged_df is not None:
    # write the merged dataframe to a new CSV file
    merged_df.to_csv("merged.csv", index=False)
else:
    print("No CSV files found in folder:", folder)

