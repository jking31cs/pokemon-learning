import json
import csv
import numpy
import os,sys
import pandas as pd

class Pokemon_Move_Count(object):
    def __init__(self):
        pass

    def Get_Files(self,folder_path):
        file_lst = os.listdir(folder_path)
        return file_lst

    def Get_Folders(self,main_path):
        dirs = os.listdir(main_path)
        return dirs 

    def Write_2_CSV(self,data,name):
        with open(name +'.csv', 'wb') as csv_file:
            writer = csv.writer(csv_file)
            for key, value in data.items():
                writer.writerow([key, value])
    
    def Read_CSV(self,path):
        csvfile = open(path, 'rb')
        file_data = csv.reader(csvfile)
        return file_data

    def MatchPaser(self,file_addr):
        f = open(file_addr)
        f_json = json.load(f)
        f_len = len(f_json)
        keys = list(f_json.keys())
        if f_len != 5:
            print "More than 5 matches!"
        if f_len == 5:
            match1 = f_json[keys[0]]
            match2 = f_json[keys[1]]
            match3 = f_json[keys[2]]
            match4 = f_json[keys[3]]
            match5 = f_json[keys[4]]
        f.close()
        return match1,match2,match3,match4,match5
    
    def Get_Edges(self,match):
        edges = match["edges"]
        return edges

    def Get_States(self,match):
        states = match["states"]
        return states

    def Count_in_Match(self, match):
        edges = self.Get_Edges(match)
        states = self.Get_States(match)
        edges_keys = list(edges.keys())
        states_keys = list(states.keys())
        len_states = len(states)
        #len_edges = len(edges)
        pokemon_move =[]
        for key in edges_keys:
            j = 0 
            while j in range(0, len_states-1):
                a = edges[key]["prevStateId"]
                b = states[states_keys[j]]["id"]
                if  a == b:
                    pokemon1 = states[states_keys[j]]["p1Status"]["name"]
                    pokemon2 = states[states_keys[j]]["p2Status"]["name"]
                    move1 = edges[key]["p1Move"]["move"]
                    move2 = edges[key]["p2Move"]["move"]
                    if move1 != None:
                        move1_name = move1["name"]
                        tuple1 = (pokemon1, move1_name)
                        pokemon_move.append(tuple1)
                    if move1 == None:
                        pass
                    if move2 != None:
                        move2_name = move2["name"]
                        tuple2 = (pokemon2,move2_name)
                        pokemon_move.append(tuple2)
                    if move2 == None:
                        pass
                    j += 1
                else: 
                    j += 1
        set_pm = list(set(pokemon_move))
        #len_pm =len(pokemon_move)
        stats = []
        for tup in set_pm:
            count_tup = pokemon_move.count(tup)
            stat_tup = [tup,count_tup]
            stats.append(stat_tup)
        return stats
            

    def Count_file(self,file_addr):
        match1,match2,match3,match4,match5 =self.MatchPaser(file_addr)
        match_list = [match1,match2,match3,match4,match5]
        sum_data = []
        for match in match_list:
            data = self.Count_in_Match(match)
            sum_data += data
        
        len_sum_data = len(sum_data)
        poke_move_lst = []
        for i in range(0,len_sum_data-1):
            poke_move = sum_data[i][0]
            if poke_move not in poke_move_lst:
                poke_move_lst.append(poke_move)
        stats = []
        for pm in poke_move_lst:
            add = 0
            for data in sum_data:
                if data[0] == pm:
                    add += data[1]
            stat = (pm,add)
            stats.append(stat)
        return stats

    def Count_Folder(self,folder_path):
        file_lst = self.Get_Files(folder_path)
        #folder_data = []
        folder_stats = {}
        #i = 0
        j = 0 
        for file in file_lst:
            file_addr = folder_path +'\\'+ str(file)
            file_data = self.Count_file(file_addr)
            for item in file_data:
                key_gen = ','.join(item[0])
                key = str(key_gen)
                if key not in folder_stats:
                    folder_stats[key] = item[1]

                if key in folder_stats:
                    old_v = folder_stats[key]
                    v = old_v + item[1]
                    folder_stats.update({key:v})
                #j += 1
                #print j , "is counted"
        return folder_stats
       
    def Count_All(self,main_path):
        folder_lst = self.Get_Folders(main_path)
        i = 0
        for folder in folder_lst:
            folder_path = main_path +'\\'+ str(folder)
            folder_stats = self.Count_Folder(folder_path)
            self.Write_2_CSV(folder_stats,str(folder))
            i += 1
            print i,"folders are counted."

    def Final_Stats(self,data_path):
        print data_path
        self.path = data_path
        files = self.Get_Files(data_path)
        all_stats = {}
        j = 0
        for f in files:
            csv_path = data_path +'\\'+ str(f)
            csv_data = self.Read_CSV(csv_path)
            for item in csv_data:
                key = ''.join(str(item[0]))
                if key not in all_stats:
                    all_stats[key] = item[1]

                if key in all_stats:
                    old_v = all_stats[key]
                    v = old_v + item[1]
                    all_stats.update({key:v})    
            j += 1
            print j , "CSV files counted"
        self.Write_2_CSV(all_stats,"Final_stat",)
