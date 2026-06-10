const API_BASE_URL = "http://localhost:8080/api";

// Elementos DOM
const statusServidorEl = document.getElementById("status-servidor");
const listaClientesEl = document.getElementById("lista-clientes");
const listaHospedagensEl = document.getElementById("lista-hospedagens");
const listaReservasEl = document.getElementById("lista-reservas");

const formCliente = document.getElementById("form-cliente");
const formReserva = document.getElementById("form-reserva");

const btnAtualizarHospedagens = document.getElementById("btn-atualizar-hospedagens");
const btnAtualizarReservas = document.getElementById("btn-atualizar-reservas");

// Verificar Conexão com o Servidor
async function verificarConexao() {
    try {
        const response = await fetch(`${API_BASE_URL}/hospedagens`, { method: 'GET', mode: 'cors' });
        if (response.ok) {
            statusServidorEl.textContent = "Conectado ao Servidor (Spring Boot)";
            statusServidorEl.className = "status-online";
            return true;
        }
    } catch (error) {
        statusServidorEl.textContent = "Servidor OFFLINE (Sem resposta do Spring Boot)";
        statusServidorEl.className = "status-offline";
    }
    return false;
}

// Carregar Clientes do Servidor
async function carregarClientes() {
    try {
        const response = await fetch(`${API_BASE_URL}/clientes`);
        if (response.ok) {
            const clientes = await response.json();
            listaClientesEl.innerHTML = "";
            if (clientes.length === 0) {
                listaClientesEl.innerHTML = `<tr><td colspan="4">Nenhum cliente cadastrado.</td></tr>`;
                return;
            }
            clientes.forEach(c => {
                listaClientesEl.innerHTML += `
                    <tr>
                        <td>${c.id}</td>
                        <td>${c.nome}</td>
                        <td>${c.cpf}</td>
                        <td>${c.telefone}</td>
                    </tr>
                `;
            });
        }
    } catch (error) {
        listaClientesEl.innerHTML = `<tr><td colspan="4" style="color: red;">Erro ao conectar ao servidor.</td></tr>`;
    }
}

// Carregar Hospedagens do Servidor
async function carregarHospedagens() {
    try {
        const response = await fetch(`${API_BASE_URL}/hospedagens`);
        if (response.ok) {
            const hospedagens = await response.json();
            listaHospedagensEl.innerHTML = "";
            if (hospedagens.length === 0) {
                listaHospedagensEl.innerHTML = `<tr><td colspan="6">Nenhuma hospedagem cadastrada.</td></tr>`;
                return;
            }
            hospedagens.forEach(h => {
                const dispClass = h.disponivel ? "badge-disp" : "badge-indisp";
                const dispText = h.disponivel ? "Livre" : "Ocupado";
                listaHospedagensEl.innerHTML += `
                    <tr>
                        <td>${h.id}</td>
                        <td><strong>${h.nome}</strong></td>
                        <td>${h.tipo}</td>
                        <td>${h.cidade}/${h.estado}</td>
                        <td>R$ ${h.diariaBase.toFixed(2)}</td>
                        <td><span class="badge ${dispClass}">${dispText}</span></td>
                    </tr>
                `;
            });
        }
    } catch (error) {
        listaHospedagensEl.innerHTML = `<tr><td colspan="6" style="color: red;">Erro ao conectar ao servidor.</td></tr>`;
    }
}

// Carregar Reservas do Servidor
async function carregarReservas() {
    try {
        const response = await fetch(`${API_BASE_URL}/reservas`);
        if (response.ok) {
            const reservas = await response.json();
            listaReservasEl.innerHTML = "";
            if (reservas.length === 0) {
                listaReservasEl.innerHTML = `<tr><td colspan="8">Nenhuma reserva cadastrada.</td></tr>`;
                return;
            }
            reservas.forEach(r => {
                const clienteNome = r.cliente ? r.cliente.nome : `ID: ${r.clienteId}`;
                const hospedagemNome = r.hospedagem ? r.hospedagem.nome : `ID: ${r.hospedagemId}`;
                
                let badgeClass = "badge-criada";
                if (r.status === "PROCESSADA") badgeClass = "badge-processada";
                if (r.status === "EFETIVADA") badgeClass = "badge-efetivada";
                if (r.status === "CANCELADA") badgeClass = "badge-cancelada";

                let botoesAcao = "";
                if (r.status !== "CANCELADA" && r.status !== "EFETIVADA") {
                    botoesAcao = `
                        <button onclick="efetivarReserva(${r.id})" class="btn-success">Efetivar</button>
                        <button onclick="cancelarReserva(${r.id})" class="btn-danger">Cancelar</button>
                    `;
                } else {
                    botoesAcao = `<span style="color: #7f8c8d; font-size: 12px;">Finalizado</span>`;
                }

                listaReservasEl.innerHTML += `
                    <tr>
                        <td>${r.id}</td>
                        <td>${clienteNome}</td>
                        <td>${hospedagemNome}</td>
                        <td>${r.dataEntrada}</td>
                        <td>${r.dataSaida}</td>
                        <td><strong>R$ ${r.valorTotal.toFixed(2)}</strong></td>
                        <td><span class="badge ${badgeClass}">${r.status}</span></td>
                        <td>${botoesAcao}</td>
                    </tr>
                `;
            });
        }
    } catch (error) {
        listaReservasEl.innerHTML = `<tr><td colspan="8" style="color: red;">Erro ao conectar ao servidor.</td></tr>`;
    }
}

// Criar Cliente
formCliente.addEventListener("submit", async (e) => {
    e.preventDefault();
    const nome = document.getElementById("cliente-nome").value;
    const cpf = document.getElementById("cliente-cpf").value;
    const telefone = document.getElementById("cliente-telefone").value;

    const payload = { nome, cpf, telefone };

    try {
        const response = await fetch(`${API_BASE_URL}/clientes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Cliente cadastrado com sucesso!");
            formCliente.reset();
            carregarClientes();
        } else {
            const erro = await response.json();
            alert(`Falha ao cadastrar: ${erro.message || JSON.stringify(erro.details)}`);
        }
    } catch (error) {
        alert("Erro de conexão ao enviar dados.");
    }
});

// Criar Solicitação de Reserva
formReserva.addEventListener("submit", async (e) => {
    e.preventDefault();
    const clienteId = parseInt(document.getElementById("reserva-cliente-id").value);
    const hospedagemId = parseInt(document.getElementById("reserva-hospedagem-id").value);
    const dataEntrada = document.getElementById("reserva-entrada").value;
    const dataSaida = document.getElementById("reserva-saida").value;

    const payload = { clienteId, hospedagemId, dataEntrada, dataSaida };

    try {
        const response = await fetch(`${API_BASE_URL}/reservas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const reserva = await response.json();
            alert(`Reserva solicitada via RabbitMQ! Código: ${reserva.id}. Status inicial: ${reserva.status}`);
            formReserva.reset();
            carregarReservas();
            carregarHospedagens();
        } else {
            const erro = await response.json();
            alert(`Falha ao solicitar reserva: ${erro.message || JSON.stringify(erro.details)}`);
        }
    } catch (error) {
        alert("Erro de conexão ao enviar dados.");
    }
});

// Cancelar Reserva (Assíncrono via RabbitMQ)
async function cancelarReserva(id) {
    if (!confirm(`Deseja solicitar o cancelamento da reserva ID ${id}?`)) return;
    try {
        const response = await fetch(`${API_BASE_URL}/reservas/${id}/cancelar`, {
            method: 'PUT'
        });
        if (response.ok) {
            alert(`Solicitação de cancelamento enviada ao RabbitMQ para reserva ID ${id}!`);
            carregarReservas();
        } else {
            const erro = await response.json();
            alert(`Erro: ${erro.message}`);
        }
    } catch (error) {
        alert("Erro ao enviar solicitação.");
    }
}

// Efetivar Reserva (Assíncrono via RabbitMQ)
async function efetivarReserva(id) {
    if (!confirm(`Deseja solicitar a efetivação (check-in) da reserva ID ${id}?`)) return;
    try {
        const response = await fetch(`${API_BASE_URL}/reservas/${id}/efetivar`, {
            method: 'PUT'
        });
        if (response.ok) {
            alert(`Solicitação de efetivação enviada ao RabbitMQ para reserva ID ${id}!`);
            carregarReservas();
        } else {
            const erro = await response.json();
            alert(`Erro: ${erro.message}`);
        }
    } catch (error) {
        alert("Erro ao enviar solicitação.");
    }
}

// Atualizar botões
btnAtualizarHospedagens.addEventListener("click", carregarHospedagens);
btnAtualizarReservas.addEventListener("click", () => {
    carregarReservas();
    carregarHospedagens();
});

// Inicialização
async function init() {
    const conectado = await verificarConexao();
    if (conectado) {
        await Promise.all([
            carregarClientes(),
            carregarHospedagens(),
            carregarReservas()
        ]);
    }
}

// Rodar de início e checar conexão periodicamente
init();
setInterval(verificarConexao, 5000);
