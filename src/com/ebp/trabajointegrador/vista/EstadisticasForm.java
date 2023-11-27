package com.ebp.trabajointegrador.vista;

import com.ebp.trabajointegrador.accesodatos.EstadisticasDAO;
import com.ebp.trabajointegrador.config.SesionUsuario;
import com.ebp.trabajointegrador.modelo.reportes.IngresosPeriodo;
import com.ebp.trabajointegrador.modelo.reportes.PedidoCantidad;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;

import com.ebp.trabajointegrador.modelo.reportes.ResumenPeriodo;
import com.ebp.trabajointegrador.modelo.usuario.Permiso;
import com.ebp.trabajointegrador.modelo.usuario.Rol;
import com.ebp.trabajointegrador.modelo.usuario.Usuario;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.DefaultCategoryDataset;

public class EstadisticasForm extends JFrame {
    private EstadisticasDAO estadisticasDAO;
    private JPanel panelMain;
    private JPanel panel;
    private JButton btnActualizar;
    private JPanel panelChartCantidad;
    private JPanel panelChartIngresos;
    private JPanel panelChartPedidos;
    private ChartPanel chartPanelCantidad;
    private ChartPanel chartPanelIngresos;
    private JScrollPane panelResumen;

    public EstadisticasForm(Connection conn) throws IOException {
        InputStream iconStream = getClass().getResourceAsStream("/com/ebp/trabajointegrador/resources/logo_pizzeria_112_100.png");
        BufferedImage myImg = null;
        if (iconStream != null) {
            myImg = ImageIO.read(iconStream);
            setIconImage(myImg);
        }

        this.estadisticasDAO = new EstadisticasDAO(conn);

        setTitle("Pizzería - Estadísticas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.add(panelMain);

        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                obtenerPizzasMasVendidas();

                obtenerIngresosPorPeriodo();

                obtenerResumenPeriodo();
            }
        });

        obtenerPizzasMasVendidas();

        obtenerIngresosPorPeriodo();

        obtenerResumenPeriodo();
    }

    private void obtenerPizzasMasVendidas()
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        var pedidosCantidades = estadisticasDAO.obtenerCantidadPedidosPorPizza();

        for (PedidoCantidad pedidoCantidad : pedidosCantidades) {
            dataset.addValue(pedidoCantidad.getCantidadPedidos(), pedidoCantidad.getVariedadPizza(), pedidoCantidad.getTipoPizza());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Cantidad de Pedidos por Variedad y Tipo de Pizza",
                "Variedad y Tipo de Pizza",
                "Cantidad de Pedidos",
                dataset
        );

        // Ajustar el rango del eje Y para mostrar solo valores enteros
        chart.getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        ChartPanel newChartPanel = new ChartPanel(chart);

        // Reemplazar el gráfico existente en el panel
        if (chartPanelCantidad != null) {
            panelChartCantidad.remove(chartPanelCantidad);  // Eliminar el gráfico anterior del contenedor
        }
        chartPanelCantidad = newChartPanel;  // Actualizar la referencia al nuevo gráfico
        panelChartCantidad.add(chartPanelCantidad);  // Agregar el nuevo gráfico al contenedor
        panelChartCantidad.revalidate();  // Revalidar el panel para actualizar la interfaz
    }

    private void obtenerIngresosPorPeriodo()
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        var ingresosPorPeriodo = estadisticasDAO.obtenerPedidosPorPeriodo();

        for (IngresosPeriodo ingresosPeriodo : ingresosPorPeriodo) {
            dataset.addValue(ingresosPeriodo.getIngresos(), "Ingresos", ingresosPeriodo.getPeriodo());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Ingresos por Período",
                "Período",
                "Ingresos",
                dataset
        );

        ChartPanel newChartPanel = new ChartPanel(chart);

        // Reemplazar el gráfico existente en el panel
        if (chartPanelIngresos != null) {
            panelChartIngresos.remove(chartPanelIngresos);  // Eliminar el gráfico anterior del contenedor
        }
        chartPanelIngresos = newChartPanel;  // Actualizar la referencia al nuevo gráfico
        panelChartIngresos.add(chartPanelIngresos);  // Agregar el nuevo gráfico al contenedor
        panelChartIngresos.revalidate();  // Revalidar el panel para actualizar la interfaz
    }

    private void obtenerResumenPeriodo()
    {
        var resumenPeriodos = estadisticasDAO.obtenerResumenPeriodo();

        // Crear la matriz de datos para la tabla
        String[] columnas = {"Periodo", "Cantidad", "Monto"};
        Object[][] datos = new Object[resumenPeriodos.size()][3];

        int fila = 0;
        for (ResumenPeriodo resumen : resumenPeriodos) {
            datos[fila][0] = resumen.getPeriodo();
            datos[fila][1] = resumen.getCantidadPedidos();
            datos[fila][2] = resumen.getMontoPedidos();
            fila++;
        }

        // Crear el modelo de tabla con los datos y columnas
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla de solo lectura
            }
        };

        // Crear la tabla y asignarle el modelo
        JTable tabla = new JTable(modelo);

        // Agregar la tabla a un JScrollPane para permitir desplazamiento si hay muchos datos
        JScrollPane scrollPane = new JScrollPane(tabla);

        // Reemplazar el componente existente en el panel
        if (panelResumen != null) {
            panelChartPedidos.remove(panelResumen);  // Eliminar el componente anterior del contenedor
        }
        panelResumen = scrollPane;  // Actualizar la referencia al nuevo componente
        panelChartPedidos.add(panelResumen);  // Agregar el nuevo componente al contenedor
        panelChartPedidos.revalidate();  // Revalidar el panel para actualizar la interfaz
    }

}
