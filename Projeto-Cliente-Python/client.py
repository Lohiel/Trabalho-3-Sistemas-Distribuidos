#!/usr/bin/env python3
"""
Cliente Python — Sistema de Hotelaria Distribuído
Disciplina: Sistemas Distribuídos — Trabalhos 3 e 4
"""

import requests
import json
from datetime import datetime

API_BASE_URL = "http://localhost:8080/api"


def verificar_conexao():
    try:
        r = requests.get(f"{API_BASE_URL}/hospedagens", timeout=3)
        return r.status_code == 200
    except requests.exceptions.RequestException:
        return False


def cabecalho():
    print("\n" + "=" * 58)
    print("     SISTEMA DE HOTELARIA — CLIENTE PYTHON")
    print("     Sistemas Distribuídos — REST + RabbitMQ")
    print("=" * 58)


def menu():
    cabecalho()
    print("  [1]  Listar Hospedagens")
    print("  [2]  Listar Clientes")
    print("  [3]  Cadastrar Novo Cliente")
    print("  [4]  Solicitar Reserva  (→ RabbitMQ)")
    print("  [5]  Listar Reservas")
    print("  [6]  Cancelar Reserva   (→ RabbitMQ)")
    print("  [7]  Efetivar Reserva   (→ RabbitMQ)")
    print("  [8]  Verificar Conexão com o Servidor")
    print("  [9]  Sair")
    print("=" * 58)
    return input("  Opção: ").strip()


# ---------- HOSPEDAGENS ----------

def listar_hospedagens():
    try:
        r = requests.get(f"{API_BASE_URL}/hospedagens")
        r.raise_for_status()
        dados = r.json()
        if not dados:
            print("\n  Nenhuma hospedagem cadastrada.")
            return
        print(f"\n{'ID':<5} {'Nome':<22} {'Tipo':<10} {'Cidade/UF':<16} {'Diária':>10} {'Disponível':>12}")
        print("-" * 78)
        for h in dados:
            disp = "✓ Livre" if h['disponivel'] else "✗ Ocupado"
            local = f"{h['cidade']}/{h['estado']}"
            print(f"{h['id']:<5} {h['nome']:<22} {h['tipo']:<10} {local:<16} "
                  f"R$ {h['diariaBase']:>7.2f} {disp:>12}")
    except requests.exceptions.ConnectionError:
        print("\n  [ERRO] Servidor offline. Verifique se o Spring Boot está em execução.")
    except requests.exceptions.HTTPError as e:
        print(f"\n  [ERRO] {e}")


# ---------- CLIENTES ----------

def listar_clientes():
    try:
        r = requests.get(f"{API_BASE_URL}/clientes")
        r.raise_for_status()
        dados = r.json()
        if not dados:
            print("\n  Nenhum cliente cadastrado.")
            return
        print(f"\n{'ID':<5} {'Nome':<25} {'CPF':<16} {'Telefone':<16}")
        print("-" * 65)
        for c in dados:
            print(f"{c['id']:<5} {c['nome']:<25} {c['cpf']:<16} {c['telefone']:<16}")
    except requests.exceptions.ConnectionError:
        print("\n  [ERRO] Servidor offline.")
    except requests.exceptions.HTTPError as e:
        print(f"\n  [ERRO] {e}")


def cadastrar_cliente():
    print("\n  --- CADASTRAR NOVO CLIENTE ---")
    nome = input("  Nome completo: ").strip()
    cpf = input("  CPF (ex: 123.456.789-00): ").strip()
    telefone = input("  Telefone: ").strip()

    if not all([nome, cpf, telefone]):
        print("  [ERRO] Todos os campos são obrigatórios.")
        return

    try:
        r = requests.post(f"{API_BASE_URL}/clientes",
                          json={"nome": nome, "cpf": cpf, "telefone": telefone})
        if r.status_code == 201:
            c = r.json()
            print(f"\n  [OK] Cliente cadastrado! ID: {c['id']} — {c['nome']}")
        else:
            err = r.json()
            print(f"\n  [ERRO] {err.get('message', err)}")
    except requests.exceptions.ConnectionError:
        print("\n  [ERRO] Servidor offline.")


# ---------- RESERVAS ----------

def solicitar_reserva():
    print("\n  --- SOLICITAR NOVA RESERVA ---")
    print("  (A reserva é salva como CRIADA e publicada no RabbitMQ para processamento assíncrono)")
    try:
        cliente_id = int(input("  ID do Cliente: "))
        hospedagem_id = int(input("  ID da Hospedagem: "))
        entrada = input("  Data de Entrada (AAAA-MM-DD): ").strip()
        saida = input("  Data de Saída   (AAAA-MM-DD): ").strip()
        # Validação básica de formato
        datetime.strptime(entrada, "%Y-%m-%d")
        datetime.strptime(saida, "%Y-%m-%d")
    except ValueError:
        print("  [ERRO] ID inválido ou data fora do formato AAAA-MM-DD.")
        return

    payload = {
        "clienteId": cliente_id,
        "hospedagemId": hospedagem_id,
        "dataEntrada": entrada,
        "dataSaida": saida
    }

    try:
        r = requests.post(f"{API_BASE_URL}/reservas", json=payload)
        if r.status_code == 201:
            res = r.json()
            print("\n" + "*" * 58)
            print("  [OK] RESERVA SOLICITADA!")
            print(f"  Código da Reserva : {res['id']}")
            print(f"  Status Inicial    : {res['status']}")
            print(f"  Valor Total       : R$ {res['valorTotal']:.2f}")
            print("*" * 58)
            print("  Evento publicado no RabbitMQ.")
            print("  Aguarde o processamento e verifique o status na listagem.")
        else:
            err = r.json()
            print(f"\n  [ERRO] {err.get('message', err)}")
    except requests.exceptions.ConnectionError:
        print("\n  [ERRO] Servidor offline.")


def listar_reservas():
    try:
        r = requests.get(f"{API_BASE_URL}/reservas")
        r.raise_for_status()
        dados = r.json()
        if not dados:
            print("\n  Nenhuma reserva cadastrada.")
            return
        print(f"\n{'ID':<5} {'Cliente':<20} {'Hospedagem':<22} {'Entrada':<12} {'Saída':<12} {'Total':>10} {'Status':<12}")
        print("-" * 97)
        for res in dados:
            cliente = res['cliente']['nome'] if res.get('cliente') else "—"
            hosp = res['hospedagem']['nome'] if res.get('hospedagem') else "—"
            print(f"{res['id']:<5} {cliente:<20} {hosp:<22} {res['dataEntrada']:<12} "
                  f"{res['dataSaida']:<12} R$ {res['valorTotal']:>6.2f} {res['status']:<12}")
    except requests.exceptions.ConnectionError:
        print("\n  [ERRO] Servidor offline.")
    except requests.exceptions.HTTPError as e:
        print(f"\n  [ERRO] {e}")


def cancelar_reserva():
    print("\n  --- CANCELAR RESERVA ---")
    try:
        rid = int(input("  ID da Reserva a cancelar: "))
    except ValueError:
        print("  [ERRO] ID inválido.")
        return

    try:
        r = requests.put(f"{API_BASE_URL}/reservas/{rid}/cancelar")
        if r.status_code == 200:
            print(f"\n  [OK] Solicitação de cancelamento enviada ao RabbitMQ para Reserva ID {rid}.")
            print("  O status será atualizado assim que o consumidor processar a mensagem.")
        else:
            err = r.json()
            print(f"\n  [ERRO] {err.get('message', err)}")
    except requests.exceptions.ConnectionError:
        print("\n  [ERRO] Servidor offline.")


def efetivar_reserva():
    print("\n  --- EFETIVAR RESERVA (CHECK-IN) ---")
    try:
        rid = int(input("  ID da Reserva a efetivar: "))
    except ValueError:
        print("  [ERRO] ID inválido.")
        return

    try:
        r = requests.put(f"{API_BASE_URL}/reservas/{rid}/efetivar")
        if r.status_code == 200:
            print(f"\n  [OK] Solicitação de efetivação enviada ao RabbitMQ para Reserva ID {rid}.")
            print("  O status será atualizado assim que o consumidor processar a mensagem.")
        else:
            err = r.json()
            print(f"\n  [ERRO] {err.get('message', err)}")
    except requests.exceptions.ConnectionError:
        print("\n  [ERRO] Servidor offline.")


# ---------- MAIN ----------

def main():
    print("\n  Verificando conexão com o servidor Spring Boot...")
    if verificar_conexao():
        print("  [OK] Servidor conectado em http://localhost:8080")
    else:
        print("  [AVISO] Servidor não responde. Verifique se o Spring Boot está em execução.")

    acoes = {
        "1": listar_hospedagens,
        "2": listar_clientes,
        "3": cadastrar_cliente,
        "4": solicitar_reserva,
        "5": listar_reservas,
        "6": cancelar_reserva,
        "7": efetivar_reserva,
    }

    while True:
        opcao = menu()

        if opcao in acoes:
            acoes[opcao]()
        elif opcao == "8":
            if verificar_conexao():
                print("\n  [OK] Servidor online em http://localhost:8080")
            else:
                print("\n  [OFFLINE] Servidor Spring Boot não responde.")
        elif opcao == "9":
            print("\n  Encerrando o cliente. Até logo!\n")
            break
        else:
            print("\n  Opção inválida. Digite um número de 1 a 9.")

        input("\n  Pressione [Enter] para continuar...")


if __name__ == "__main__":
    main()
