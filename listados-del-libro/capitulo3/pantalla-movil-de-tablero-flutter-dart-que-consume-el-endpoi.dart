// lib/screens/tablero_screen.dart
import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../widgets/kpi_card.dart';

class TableroScreen extends StatefulWidget {
	final TelemetriaApi api;
	const TableroScreen({required this.api, super.key});
	@override State<TableroScreen> createState() => _TableroScreenState();
}

class _TableroScreenState extends State<TableroScreen> {
	late Future<Map<String,dynamic>> _kpis;
	
	@override
	void initState() {
		super.initState();
		_kpis = widget.api.obtenerKpi("CNT-007");
	}
	
	@override
	Widget build(BuildContext context) => Scaffold(
		appBar: AppBar(title: const Text("AgroCadena")),
		body: RefreshIndicator(
			onRefresh: () async => setState(() =>
				_kpis = widget.api.obtenerKpi("CNT-007")),
			child: FutureBuilder<Map<String,dynamic>>(
				future: _kpis,
				builder: (ctx, snap) {
					if (!snap.hasData) return const Center(child: CircularProgressIndicator());
					final k = snap.data!;
					return ListView(children: [
						KpiCard(etiqueta: "MKT",          valor: k["mkt"],          unidad: "degC", umbral: 8,  reglaMenor: true),
						KpiCard(etiqueta: "Cumplimiento", valor: k["cumplimiento"], unidad: "%",  umbral: 98, reglaMenor: false),
						KpiCard(etiqueta: "Tiempo fuera", valor: k["tor_min"],      unidad: "min",umbral: 30, reglaMenor: true),
					]);
				},
			),
		),
	);
}
