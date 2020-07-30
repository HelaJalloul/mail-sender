
#!/usr/bin/python3
# coding: utf-8

import requests
import csv
from lxml import html
import datetime
import argparse


def extract(url, path):

    """
    Export all Name/Phone from a (french) PagesJaunes Web Page

    Arguments:
         url (str):
            url of the aimed PagesJaunes Web Page
        path (str):
            path to the repository to save the .csv

    Return:
        .csv file
    """

    # INITIALISATION
    r = requests.session()
    start = datetime.datetime.now()

    # COLLECTE DU CODE SOURCE

    # on modifie les headers
    headers = {'Host': 'www.pagesjaunes.fr',
                'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:63.0) Gecko/20100101 Firefox/63.0',
                'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
                'Accept-Language': 'fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3',
                'Accept-Encoding': 'gzip, deflate, br',
                'Referer': 'https://www.pagesjaunes.fr/',
                'Content-Type': 'application/x-www-form-urlencoded',
                'Content-Length': '379',
                'Connection': 'keep-alive',
                'Upgrade-Insecure-Requests': '1',
                'Cache-Control': 'max-age=0'
               }

    # on récupere la data depuis le navigateur
    data = "quoiqui=hôtel&ou=Paris%201er%20arrondissement%20%20%2875001%29&idOu=A0750560001&contexte=/D8bjQh1eQUaFDLquxPaZA%3D%3D&proximite=1&quoiQuiInterprete=hôtel&carte=0"

    # on envoie la requête
    response = r.post(url=url, headers=headers, data=data)
    print('-- URL --')
    print(url)
    print("-- STATUS CODE --")
    print(response.status_code)

    # CREATION DU CSV
    with open(path + '/extract.csv', "w") as f:
        fieldnames = ['Name', 'Phone']
        writer = csv.DictWriter(f, fieldnames=fieldnames, delimiter='\t')
        writer.writeheader()

        # PARSING DE LA PAGE
        tree = html.fromstring(response.text)
        articles = tree.xpath("//article[contains(@id, 'bi-bloc-')]")
        for article in articles:
            name = article.xpath(".//a[@class='lvs-container marg-btm-s']/text()")
            phone = article.xpath(".//strong[@class='num']/@title")
            if name and phone:
                print(name[0].strip(), phone[0].strip())
            values = [name, phone]
            dict_row = dict(zip(fieldnames, values))
            writer.writerow(dict_row)

    # TEMPS PASSE
    end = datetime.datetime.now()
    time_elapsed = str(end-start)
    print('\n')
    print('-- TIME ELAPSED --')
    print(time_elapsed)


if __name__ == '__main__':
    argparser = argparse.ArgumentParser()
    argparser.add_argument('url', help='PagesJaunes URLs')
    argparser.add_argument('path', help='Path to csv')

    args = argparser.parse_args()
    # URL
    url = args.url
    # CHEMIN DE SAUVEGARDE DU CSV P
    path = args.path

    # ON LANCE LA FONCTION
    extract(url, path)