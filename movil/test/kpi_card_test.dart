import 'package:agrocadena_movil/widgets/kpi_card.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
	testWidgets('KpiCard muestra el valor con un decimal', (tester) async {
		await tester.pumpWidget(const MaterialApp(home: Scaffold(body:
			KpiCard(etiqueta: 'MKT', valor: 7.84, unidad: 'degC', umbral: 8, reglaMenor: true))));
		expect(find.text('MKT'), findsOneWidget);
		expect(find.text('7.8 degC'), findsOneWidget);
	});

	testWidgets('KpiCard sin datos muestra un guion', (tester) async {
		await tester.pumpWidget(const MaterialApp(home: Scaffold(body:
			KpiCard(etiqueta: 'MKT', valor: 'NaN', unidad: 'degC', umbral: 8, reglaMenor: true))));
		expect(find.text('—'), findsOneWidget);
	});
}
