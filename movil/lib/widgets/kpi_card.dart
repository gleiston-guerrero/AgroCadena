// lib/widgets/kpi_card.dart -- tarjeta de indicador con semaforo (cap. 3)
import 'package:flutter/material.dart';

class KpiCard extends StatelessWidget {
	final String etiqueta;
	final dynamic valor;        // num o "NaN" cuando no hay lecturas
	final String unidad;
	final num umbral;
	final bool reglaMenor;      // true: bueno si valor <= umbral

	const KpiCard({required this.etiqueta, required this.valor,
		required this.unidad, required this.umbral, required this.reglaMenor,
		super.key});

	@override
	Widget build(BuildContext context) {
		final v = valor is num ? (valor as num).toDouble() : double.nan;
		final conDato = !v.isNaN;
		final cumple = reglaMenor ? v <= umbral : v >= umbral;
		final color = !conDato ? Colors.grey : (cumple ? Colors.green.shade700 : Colors.red.shade700);
		return Card(
			margin: const EdgeInsets.all(12),
			child: ListTile(
				leading: Icon(Icons.circle, color: color),
				title: Text(etiqueta),
				subtitle: Text("Umbral: $umbral $unidad"),
				trailing: Text(conDato ? "${v.toStringAsFixed(1)} $unidad" : "—",
					style: Theme.of(context).textTheme.headlineSmall),
			),
		);
	}
}
