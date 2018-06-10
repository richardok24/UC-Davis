#Richard Xie

"""
A script that reads single monthly tick data csv file.

Can be used to detect any flash crash based on the percent change
of min and max ask price for each day’s ticks in 10 years of monthly
tick data for each currency cross. 

It will return specific dates and the percent change of those days
whenever the threshold (can be set manually) is reached.

Written in Python 3
"""

import pandas as pd

df = pd.read_csv('GBPUSD_Ticks_2016.10.06_2016.10.07.csv', sep=",", header=[0], parse_dates=["Time (UTC)"])
df.set_index("Time (UTC)", drop=True, inplace=True)
daily_ask = df.resample("D")["Ask"]
df["daily_ask_min"] = daily_ask.transform("min")
df["daily_ask_max"] = daily_ask.transform("max")
df["daily_ask_change"] = (df["daily_ask_max"] - df["daily_ask_min"]) / df["daily_ask_max"]
print("\n")
print(df[df.daily_ask_change > 0.05]["daily_ask_change"].resample("D").mean())