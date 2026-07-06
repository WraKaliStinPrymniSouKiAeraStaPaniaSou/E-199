<?php
// E-199 Admin Dashboard - Standalone PHP
header('Content-Type: text/html; charset=UTF-8');

$DB_HOST = 'localhost';
$DB_NAME = 'e199';
$DB_USER = 'root';
$DB_PASS = '';

try {
    $pdo = new PDO("mysql:host=$DB_HOST;dbname=$DB_NAME;charset=utf8mb4", $DB_USER, $DB_PASS);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("<h1 style='color:red;text-align:center;margin-top:50px;'>Database connection failed: " . htmlspecialchars($e->getMessage()) . "</h1>");
}

// ---- Stats queries ----
$floodCount    = $pdo->query("SELECT COUNT(*) FROM incidents WHERE incident_type='flood'")->fetchColumn();
$earthquakeCount = $pdo->query("SELECT COUNT(*) FROM incidents WHERE incident_type='earthquake'")->fetchColumn();
$fireCount     = $pdo->query("SELECT COUNT(*) FROM incidents WHERE incident_type='fire'")->fetchColumn();
$accidentCount = $pdo->query("SELECT COUNT(*) FROM incidents WHERE incident_type='accident'")->fetchColumn();
$vehiclesCount = $pdo->query("SELECT COALESCE(SUM(vehicles),0) FROM incidents WHERE status IN ('running','finished')")->fetchColumn();
$firemenCount  = $pdo->query("SELECT COALESCE(SUM(firemen),0) FROM incidents WHERE status IN ('running','finished')")->fetchColumn();
$usersCount    = $pdo->query("SELECT COUNT(*) FROM users")->fetchColumn();
$volunteersCount = $pdo->query("SELECT COUNT(*) FROM volunteers")->fetchColumn();
$incidentsCount  = $pdo->query("SELECT COUNT(*) FROM incidents")->fetchColumn();
$messagesCount   = $pdo->query("SELECT COUNT(*) FROM messages")->fetchColumn();
$participantsCount = $pdo->query("SELECT COUNT(*) FROM participants")->fetchColumn();

// ---- Fetch all data ----
$incidents   = $pdo->query("SELECT * FROM incidents ORDER BY incident_id DESC")->fetchAll(PDO::FETCH_ASSOC);
$users       = $pdo->query("SELECT * FROM users ORDER BY user_id DESC")->fetchAll(PDO::FETCH_ASSOC);
$volunteers  = $pdo->query("SELECT * FROM volunteers ORDER BY volunteer_id DESC")->fetchAll(PDO::FETCH_ASSOC);
$messages    = $pdo->query("SELECT m.*, i.address as inc_address FROM messages m LEFT JOIN incidents i ON m.incident_id=i.incident_id ORDER BY m.message_id DESC")->fetchAll(PDO::FETCH_ASSOC);
$participants = $pdo->query("SELECT p.*, i.address as inc_address FROM participants p LEFT JOIN incidents i ON p.incident_id=i.incident_id ORDER BY p.participant_id DESC")->fetchAll(PDO::FETCH_ASSOC);
?>
<!DOCTYPE html>
<html>
<head>
    <title>E-199 Admin Dashboard</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            font-family: 'Times New Roman', Times, serif;
            background-color: rgb(90, 56, 35);
            margin: 0;
            padding: 20px;
            padding-top: 60px;
            min-height: 100vh;
            color: white;
        }
        .navbar {
            width: 100%;
            background-color: #6b4423;
            padding: 8px 20px;
            box-shadow: 0px 2px 4px rgba(0,0,0,0.3);
            display: flex;
            justify-content: flex-end;
            align-items: center;
            position: fixed;
            top: 0;
            left: 0;
            z-index: 1000;
            height: 40px;
            gap: 10px;
        }
        .navbar button, .navbar a {
            padding: 5px 12px;
            font-size: 13px;
            color: #fff;
            background-color: #ff7043;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }
        .navbar button:hover, .navbar a:hover { background-color: #d95c26; }
        .navbar .brand {
            margin-right: auto;
            color: rgb(210,180,140);
            font-weight: bold;
            font-size: 16px;
        }
        h1, h2, h3 { color: rgb(210,180,140); margin-bottom: 15px; }
        h1 { font-size: 26px; }
        h2 { font-size: 20px; margin-top: 30px; border-bottom: 2px solid rgb(210,180,140); padding-bottom: 5px; }
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
            gap: 15px;
            margin: 20px 0;
        }
        .stat-card {
            background: rgb(60,40,25);
            padding: 18px;
            border-radius: 10px;
            box-shadow: 0px 4px 8px rgba(0,0,0,0.3);
            text-align: center;
        }
        .stat-card .number { font-size: 32px; font-weight: bold; color: #ff7043; }
        .stat-card .label { font-size: 13px; color: rgb(210,180,140); margin-top: 5px; }
        .charts-row {
            display: flex;
            flex-wrap: wrap;
            gap: 30px;
            justify-content: center;
            margin: 20px 0;
        }
        .chart-box {
            background: rgb(60,40,25);
            padding: 15px;
            border-radius: 10px;
            box-shadow: 0px 4px 8px rgba(0,0,0,0.3);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 15px 0;
            background: rgb(60,40,25);
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0px 4px 8px rgba(0,0,0,0.3);
            font-size: 13px;
        }
        th, td { padding: 8px 10px; text-align: left; border-bottom: 1px solid rgb(90,56,35); }
        th { background: #6b4423; color: rgb(210,180,140); font-weight: bold; }
        td { color: #ddd; }
        tr:hover td { background: rgba(255,112,67,0.1); }
        .badge {
            display: inline-block;
            padding: 2px 8px;
            border-radius: 10px;
            font-size: 11px;
            font-weight: bold;
        }
        .badge-running { background: #4CAF50; color: white; }
        .badge-finished { background: #2196F3; color: white; }
        .badge-fake { background: #f44336; color: white; }
        .badge-high { background: #f44336; color: white; }
        .badge-medium { background: #FF9800; color: white; }
        .badge-low { background: #4CAF50; color: white; }
        .badge-fire { background: #ff5722; }
        .badge-flood { background: #2196F3; }
        .badge-earthquake { background: #795548; }
        .badge-accident { background: #9C27B0; }
        .badge-accepted { background: #4CAF50; }
        .badge-pending { background: #FF9800; }
        .badge-finished-status { background: #2196F3; }
        .section-toggle {
            background: rgb(60,40,25);
            border: 1px solid rgb(210,180,140);
            color: rgb(210,180,140);
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            margin: 5px;
            transition: all 0.3s ease;
        }
        .section-toggle:hover {
            background: #ff7043;
            color: white;
            border-color: #ff7043;
        }
        .section-toggle.active {
            background: #ff7043;
            color: white;
        }
        .tabs { text-align: center; margin: 20px 0; }
        .section { display: none; }
        .section.active { display: block; }
        .scroll-table { overflow-x: auto; }
        ::-webkit-scrollbar { width: 8px; height: 8px; }
        ::-webkit-scrollbar-track { background: rgb(60,40,25); }
        ::-webkit-scrollbar-thumb { background: #6b4423; border-radius: 4px; }
    </style>
</head>
<body>

<div class="navbar">
    <span class="brand">E-199 Admin Dashboard</span>
    <button onclick="showSection('stats')">Stats</button>
    <button onclick="showSection('incidents')">Incidents (<?= $incidentsCount ?>)</button>
    <button onclick="showSection('users')">Users (<?= $usersCount ?>)</button>
    <button onclick="showSection('volunteers')">Volunteers (<?= $volunteersCount ?>)</button>
    <button onclick="showSection('messages')">Messages (<?= $messagesCount ?>)</button>
    <button onclick="showSection('participants')">Participants (<?= $participantsCount ?>)</button>
</div>

<!-- Stats Section -->
<div id="section-stats" class="section active">
    <h1>Statistics Overview</h1>

    <div class="dashboard-grid">
        <div class="stat-card"><div class="number"><?= $incidentsCount ?></div><div class="label">Total Incidents</div></div>
        <div class="stat-card"><div class="number"><?= $usersCount ?></div><div class="label">Users</div></div>
        <div class="stat-card"><div class="number"><?= $volunteersCount ?></div><div class="label">Volunteers</div></div>
        <div class="stat-card"><div class="number"><?= $messagesCount ?></div><div class="label">Messages</div></div>
        <div class="stat-card"><div class="number"><?= $participantsCount ?></div><div class="label">Participants</div></div>
        <div class="stat-card"><div class="number"><?= $vehiclesCount ?></div><div class="label">Vehicles Used</div></div>
        <div class="stat-card"><div class="number"><?= $firemenCount ?></div><div class="label">Firemen Used</div></div>
    </div>

    <div class="charts-row">
        <div class="chart-box" id="pieChart" style="width:450px;height:300px;"></div>
        <div class="chart-box" id="barChart1" style="width:400px;height:300px;"></div>
        <div class="chart-box" id="barChart2" style="width:400px;height:300px;"></div>
    </div>
</div>

<!-- Incidents Section -->
<div id="section-incidents" class="section">
    <h2>All Incidents</h2>
    <div class="scroll-table">
    <table>
        <thead>
            <tr>
                <th>ID</th><th>Type</th><th>Description</th><th>Phone</th><th>Address</th>
                <th>Municipality</th><th>Prefecture</th><th>Danger</th><th>Status</th>
                <th>Vehicles</th><th>Firemen</th><th>Start</th><th>End</th><th>Result</th>
            </tr>
        </thead>
        <tbody>
        <?php foreach ($incidents as $inc): ?>
            <tr>
                <td><?= $inc['incident_id'] ?></td>
                <td><span class="badge badge-<?= $inc['incident_type'] ?>"><?= htmlspecialchars($inc['incident_type']) ?></span></td>
                <td><?= htmlspecialchars(mb_substr($inc['description'],0,40)) ?></td>
                <td><?= htmlspecialchars($inc['user_phone']) ?></td>
                <td><?= htmlspecialchars($inc['address']) ?></td>
                <td><?= htmlspecialchars($inc['municipality']) ?></td>
                <td><?= htmlspecialchars($inc['prefecture']) ?></td>
                <td><span class="badge badge-<?= $inc['danger'] ?>"><?= htmlspecialchars($inc['danger']) ?></span></td>
                <td><span class="badge badge-<?= $inc['status'] ?>"><?= htmlspecialchars($inc['status']) ?></span></td>
                <td><?= $inc['vehicles'] ?></td>
                <td><?= $inc['firemen'] ?></td>
                <td><?= $inc['start_datetime'] ?></td>
                <td><?= $inc['end_datetime'] ?? '-' ?></td>
                <td><?= htmlspecialchars(mb_substr($inc['finalResult']??'',0,30)) ?></td>
            </tr>
        <?php endforeach; ?>
        </tbody>
    </table>
    </div>
</div>

<!-- Users Section -->
<div id="section-users" class="section">
    <h2>All Users</h2>
    <div class="scroll-table">
    <table>
        <thead>
            <tr>
                <th>ID</th><th>Username</th><th>Email</th><th>Firstname</th><th>Lastname</th>
                <th>Birthdate</th><th>Gender</th><th>AFM</th><th>Country</th>
                <th>Address</th><th>Municipality</th><th>Prefecture</th><th>Job</th><th>Telephone</th>
            </tr>
        </thead>
        <tbody>
        <?php foreach ($users as $u): ?>
            <tr>
                <td><?= $u['user_id'] ?></td>
                <td><?= htmlspecialchars($u['username']) ?></td>
                <td><?= htmlspecialchars($u['email']) ?></td>
                <td><?= htmlspecialchars($u['firstname']) ?></td>
                <td><?= htmlspecialchars($u['lastname']) ?></td>
                <td><?= $u['birthdate'] ?></td>
                <td><?= htmlspecialchars($u['gender']) ?></td>
                <td><?= htmlspecialchars($u['afm']) ?></td>
                <td><?= htmlspecialchars($u['country']) ?></td>
                <td><?= htmlspecialchars($u['address']) ?></td>
                <td><?= htmlspecialchars($u['municipality']) ?></td>
                <td><?= htmlspecialchars($u['prefecture']) ?></td>
                <td><?= htmlspecialchars($u['job']) ?></td>
                <td><?= htmlspecialchars($u['telephone']) ?></td>
            </tr>
        <?php endforeach; ?>
        </tbody>
    </table>
    </div>
</div>

<!-- Volunteers Section -->
<div id="section-volunteers" class="section">
    <h2>All Volunteers</h2>
    <div class="scroll-table">
    <table>
        <thead>
            <tr>
                <th>ID</th><th>Username</th><th>Email</th><th>Firstname</th><th>Lastname</th>
                <th>Birthdate</th><th>Gender</th><th>AFM</th><th>Vol. Type</th>
                <th>Address</th><th>Municipality</th><th>Prefecture</th><th>Telephone</th><th>Height</th><th>Weight</th>
            </tr>
        </thead>
        <tbody>
        <?php foreach ($volunteers as $v): ?>
            <tr>
                <td><?= $v['volunteer_id'] ?></td>
                <td><?= htmlspecialchars($v['username']) ?></td>
                <td><?= htmlspecialchars($v['email']) ?></td>
                <td><?= htmlspecialchars($v['firstname']) ?></td>
                <td><?= htmlspecialchars($v['lastname']) ?></td>
                <td><?= $v['birthdate'] ?></td>
                <td><?= htmlspecialchars($v['gender']) ?></td>
                <td><?= htmlspecialchars($v['afm']) ?></td>
                <td><?= htmlspecialchars($v['volunteer_type']) ?></td>
                <td><?= htmlspecialchars($v['address']) ?></td>
                <td><?= htmlspecialchars($v['municipality']) ?></td>
                <td><?= htmlspecialchars($v['prefecture']) ?></td>
                <td><?= htmlspecialchars($v['telephone']) ?></td>
                <td><?= $v['height'] ?></td>
                <td><?= $v['weight'] ?></td>
            </tr>
        <?php endforeach; ?>
        </tbody>
    </table>
    </div>
</div>

<!-- Messages Section -->
<div id="section-messages" class="section">
    <h2>All Messages</h2>
    <div class="scroll-table">
    <table>
        <thead>
            <tr><th>ID</th><th>Incident ID</th><th>Incident Address</th><th>Message</th><th>Sender</th><th>Recipient</th><th>Date/Time</th></tr>
        </thead>
        <tbody>
        <?php foreach ($messages as $m): ?>
            <tr>
                <td><?= $m['message_id'] ?></td>
                <td><?= $m['incident_id'] ?></td>
                <td><?= htmlspecialchars($m['inc_address']??'') ?></td>
                <td><?= htmlspecialchars(mb_substr($m['message'],0,60)) ?></td>
                <td><?= htmlspecialchars($m['sender']) ?></td>
                <td><?= htmlspecialchars($m['recipient']) ?></td>
                <td><?= $m['date_time'] ?></td>
            </tr>
        <?php endforeach; ?>
        </tbody>
    </table>
    </div>
</div>

<!-- Participants Section -->
<div id="section-participants" class="section">
    <h2>All Participants</h2>
    <div class="scroll-table">
    <table>
        <thead>
            <tr><th>ID</th><th>Incident ID</th><th>Address</th><th>Volunteer</th><th>Vol. Type</th><th>Status</th><th>Success</th><th>Comment</th></tr>
        </thead>
        <tbody>
        <?php foreach ($participants as $p): ?>
            <tr>
                <td><?= $p['participant_id'] ?></td>
                <td><?= $p['incident_id'] ?></td>
                <td><?= htmlspecialchars($p['inc_address']??'') ?></td>
                <td><?= htmlspecialchars($p['volunteer_username']??'-') ?></td>
                <td><?= htmlspecialchars($p['volunteer_type']) ?></td>
                <td><span class="badge badge-<?= $p['status'] ?>"><?= htmlspecialchars($p['status']) ?></span></td>
                <td><?= htmlspecialchars($p['success']??'-') ?></td>
                <td><?= htmlspecialchars(mb_substr($p['comment']??'',0,40)) ?></td>
            </tr>
        <?php endforeach; ?>
        </tbody>
    </table>
    </div>
</div>

<script>
google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(drawCharts);

function drawCharts() {
    // Pie chart - Incidents by type
    var pieData = google.visualization.arrayToDataTable([
        ['Type', 'Count'],
        ['Flood',     <?= $floodCount ?>],
        ['Earthquake',<?= $earthquakeCount ?>],
        ['Fire',      <?= $fireCount ?>],
        ['Accident',  <?= $accidentCount ?>]
    ]);
    var pieOpts = { title: 'Incidents by Type', pieHole: 0.4, backgroundColor: 'transparent',
        titleTextStyle: { color: 'rgb(210,180,140)' },
        legend: { textStyle: { color: '#ddd' } } };
    new google.visualization.PieChart(document.getElementById('pieChart')).draw(pieData, pieOpts);

    // Bar chart - Firemen & Vehicles
    var fvData = google.visualization.arrayToDataTable([
        ['Category', 'Count'],
        ['Firemen',  <?= $firemenCount ?>],
        ['Vehicles', <?= $vehiclesCount ?>]
    ]);
    var fvOpts = { title: 'Firemen & Vehicles', backgroundColor: 'transparent',
        titleTextStyle: { color: 'rgb(210,180,140)' },
        legend: { textStyle: { color: '#ddd' } },
        hAxis: { textStyle: { color: '#ddd' } },
        vAxis: { textStyle: { color: '#ddd' } } };
    new google.visualization.BarChart(document.getElementById('barChart1')).draw(fvData, fvOpts);

    // Bar chart - Users & Volunteers
    var uvData = google.visualization.arrayToDataTable([
        ['Category', 'Count'],
        ['Users',     <?= $usersCount ?>],
        ['Volunteers',<?= $volunteersCount ?>]
    ]);
    var uvOpts = { title: 'Users & Volunteers', backgroundColor: 'transparent',
        titleTextStyle: { color: 'rgb(210,180,140)' },
        legend: { textStyle: { color: '#ddd' } },
        hAxis: { textStyle: { color: '#ddd' } },
        vAxis: { textStyle: { color: '#ddd' } } };
    new google.visualization.BarChart(document.getElementById('barChart2')).draw(uvData, uvOpts);
}

function showSection(name) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.getElementById('section-' + name).classList.add('active');
    google.charts.setOnLoadCallback(drawCharts);
}
</script>

</body>
</html>
