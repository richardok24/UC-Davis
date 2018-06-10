#Richard Xie

"""
A script that reads multiple monthly tick data csv files.

Can be used to detect any flash crash based on the percent change
of min and max ask price for each day’s ticks in 10 years of monthly
tick data for each currency cross. 

It will return specific dates and the percent change of those days
whenever the threshold (can be set manually) is reached.

Written in Python 3
"""

import pandas as pd
from os import listdir, getcwd
from os.path import isfile, isdir, join, splitext, basename, abspath
import time

QUIET = 0 #set to 1 to suppress progam feedback


data_dir = abspath("D:/John-tick-data/2007")

#Process months (Kevin's code for reading multiple csv files)
months = [month for month in listdir(data_dir) if (isdir(join(data_dir, month)))]
for month in months:
    if not QUIET: print("\nProcessing " + month + " data:\n")

    #Save the absolute path to the current month
    month_dir = join(data_dir, month)

    #Select all .csv files in the current directory
    currency_crosses = [currency for currency in listdir(month_dir) if \
    ( isfile(join(month_dir, currency)) and (splitext(currency)[1] == '.csv') )]

    #Process tick data (Print out the date based on the percent change of ask price)
    for currency_cross in currency_crosses:
        print("Processing " + currency_cross[0:6] + ":")
        df = pd.read_csv(join(month_dir,currency_cross), sep=',', header=[0], parse_dates=["Time (UTC)"])
        df.set_index("Time (UTC)", drop=True, inplace=True)
        daily_ask = df.resample("D")["Ask"]
        df["daily_ask_min"] = daily_ask.transform("min")
        df["daily_ask_max"] = daily_ask.transform("max")
        df["daily_ask_change"] = (df["daily_ask_max"] - df["daily_ask_min"]) / df["daily_ask_max"]
        # Change the percent change threshold below
        print(df[df.daily_ask_change > 0.05]["daily_ask_change"].resample("D").mean())
        print("\n")
        #break #single currency
    #break #single month