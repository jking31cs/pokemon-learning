from Pokemon_Move_Count import Pokemon_Move_Count
import json
import numpy

data_path = "/Users/jking31cs/git-work/pokemon-learning/output"

def main(path):
     #match1,match2,match3,match4,match5 = Pokemon_Move_Count().MatchPaser(test_addr)
     #result = Pokemon_Move_Count().Count_in_Match(match1)
     #result = Pokemon_Move_Count().Get_Files(test_folder)
     #result = Pokemon_Move_Count().Count_All(path)
     result =  Pokemon_Move_Count().Final_Stats(path)
     return result

if __name__ == '__main__':
    main(data_path)
