const tables = [
    { name: 'incidents', label: 'Incidents', idCol: 'incident_id' },
    { name: 'users', label: 'Users', idCol: 'user_id' },
    { name: 'volunteers', label: 'Volunteers', idCol: 'volunteer_id' },
    { name: 'messages', label: 'Messages', idCol: 'message_id' },
    { name: 'participants', label: 'Participants', idCol: 'participant_id' }
];

const colorMap = {
    running: '#4CAF50', finished: '#2196F3', fake: '#f44336',
    high: '#f44336', medium: '#FF9800', low: '#4CAF50',
    fire: '#ff5722', flood: '#2196F3', earthquake: '#795548', accident: '#9C27B0',
    accepted: '#4CAF50', pending: '#FF9800'
};

async function loadAll() {
    const container = document.getElementById('tables-container');
    container.innerHTML = '<div class="loading">Loading data...</div>';

    for (const table of tables) {
        try {
            const resp = await fetch('/E-199/AdminManageData?type=' + table.name);
            if (!resp.ok) {
                if (resp.status === 401) {
                    window.location.href = '/E-199/html/admin_InitialPage.html';
                    return;
                }
                continue;
            }
            const result = await resp.json();
            if (result.success) {
                renderTable(table, result.data);
            }
        } catch (e) {
            console.error('Error loading ' + table.name, e);
        }
    }
}

function renderTable(table, data) {
    const container = document.getElementById('tables-container');
    if (data.length === 0) {
        const emptyMsg = document.createElement('p');
        emptyMsg.style.color = 'rgb(210,180,140)';
        emptyMsg.textContent = table.label + ': 0 records';
        container.appendChild(emptyMsg);
        return;
    }

    const section = document.createElement('div');
    section.className = 'data-section';

    const header = document.createElement('h2');
    header.textContent = table.label + ' (' + data.length + ')';
    section.appendChild(header);

    const wrap = document.createElement('div');
    wrap.className = 'scroll-wrap';

    const tableEl = document.createElement('table');
    const thead = document.createElement('thead');
    const tbody = document.createElement('tbody');

    const columns = Object.keys(data[0]);
    let idIdx = columns.indexOf(table.idCol);

    // header row
    const trHead = document.createElement('tr');
    const thAction = document.createElement('th');
    thAction.textContent = 'Action';
    trHead.appendChild(thAction);
    columns.forEach(col => {
        const th = document.createElement('th');
        th.textContent = col;
        trHead.appendChild(th);
    });
    thead.appendChild(trHead);
    tableEl.appendChild(thead);

    // data rows
    data.forEach(row => {
        const tr = document.createElement('tr');
        const tdAction = document.createElement('td');
        const deleteBtn = document.createElement('button');
        deleteBtn.className = 'delete-btn';
        deleteBtn.textContent = 'Delete';
        deleteBtn.onclick = () => confirmDelete(table.name, row[table.idCol], deleteBtn);
        tdAction.appendChild(deleteBtn);
        tr.appendChild(tdAction);

        columns.forEach((col, idx) => {
            const td = document.createElement('td');
            let val = row[col] || '';
            if (idx === idIdx + 1) {
                const span = document.createElement('span');
                span.textContent = val;
                const color = colorMap[val.toLowerCase()];
                if (color) {
                    span.style.cssText = 'display:inline-block;padding:2px 8px;border-radius:10px;font-size:11px;font-weight:bold;background:' + color + ';color:white;';
                }
                td.appendChild(span);
            } else {
                td.textContent = val;
            }
            tr.appendChild(td);
        });

        tbody.appendChild(tr);
    });

    tableEl.appendChild(tbody);
    wrap.appendChild(tableEl);
    section.appendChild(wrap);
    container.appendChild(section);
}

async function confirmDelete(type, id, btn) {
    if (!confirm('Delete ' + type + ' with ID ' + id + '? This cannot be undone.')) return;
    btn.disabled = true;
    btn.textContent = '...';

    try {
        const resp = await fetch('/E-199/AdminManageData', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'type=' + type + '&id=' + id
        });
        const result = await resp.json();
        if (result.success) {
            btn.textContent = 'Deleted!';
            btn.style.backgroundColor = '#555';
            const row = btn.closest('tr');
            row.style.opacity = '0.3';
        } else {
            alert('Error: ' + (result.message || 'Unknown error'));
            btn.disabled = false;
            btn.textContent = 'Delete';
        }
    } catch (e) {
        alert('Error deleting record');
        btn.disabled = false;
        btn.textContent = 'Delete';
    }
}

document.addEventListener('DOMContentLoaded', loadAll);
