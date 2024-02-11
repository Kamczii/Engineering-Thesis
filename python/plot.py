# importing the required module
import matplotlib.pyplot as plt
import mplcyberpunk
import numpy as np

from glboals import FOLDER


if __name__ == "__main__":
    plt.style.use("cyberpunk")
    random = [
        0.34540389972144847,
        0.4818941504178273,
        0.5905292479108635,
        0.6657381615598886,
        0.7437325905292479,
        0.7910863509749304,
        0.8133704735376045,
        0.8356545961002786,
        0.8607242339832869,
        0.8662952646239555,
        0.8746518105849582,
        0.8857938718662952,
        0.8941504178272981,
        0.9025069637883009,
        0.9080779944289693,
        0.9164345403899722,
        0.9220055710306406,
        0.9303621169916435,
        0.9387186629526463,
        0.9415041782729805
    ]
    order = [
        0.403899721448468,
        0.5515320334261838,
        0.6072423398328691,
        0.6518105849582173,
        0.6768802228412256,
        0.7047353760445683,
        0.7381615598885793,
        0.754874651810585,
        0.7715877437325905,
        0.7855153203342619,
        0.8050139275766016,
        0.807799442896936,
        0.83008356545961,
        0.8328690807799443,
        0.8440111420612814,
        0.8467966573816156,
        0.8495821727019499,
        0.8523676880222841,
        0.8523676880222841,
        0.8635097493036211
    ]
    y1 = [v*100 for v in random]
    y2 = [v*100 for v in order]
    plt.plot(range(len(y1)), y1, label = 'Metoda losowania')
    plt.plot(range(len(y2)), y2, label = 'Metoda posortowania')
    plt.xlabel('Liczba połączeń')
    plt.ylabel('Szansa, że perfekcyjny przyjaciel jest z nami połączony w procentach')
    plt.title('Szansa na połączenie z perfekcyjnym przyjacielem')
    plt.xticks(np.arange(min(range(len(y1))), max(range(len(y1)))+1, 1.0))
    plt.yticks(np.arange(0, 100+1, 10.0))
    plt.legend()
    plt.savefig(f'{FOLDER}/scores.png')
