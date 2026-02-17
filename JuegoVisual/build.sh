#!/bin/bash
# ============================================================
#  Script de compilación y empaquetado del Juego Visual
#  Genera un JAR ejecutable en Ubuntu
# ============================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$SCRIPT_DIR/src"
BUILD_DIR="$SCRIPT_DIR/build"
JAR_NAME="HeroesVsDemonio.jar"

echo "🔨 Compilando Héroes vs Demonio de Fuego..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Limpiar y crear directorio de compilación
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

# Compilar
javac -encoding UTF-8 -d "$BUILD_DIR" "$SRC_DIR/JuegoGUI.java"

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
else
    echo "❌ Error de compilación"
    exit 1
fi

# Crear manifest
echo "Main-Class: JuegoGUI" > "$BUILD_DIR/MANIFEST.MF"
echo "" >> "$BUILD_DIR/MANIFEST.MF"

# Crear JAR
cd "$BUILD_DIR"
jar cfm "../$JAR_NAME" MANIFEST.MF *.class

if [ $? -eq 0 ]; then
    echo "✅ JAR creado: $SCRIPT_DIR/$JAR_NAME"
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "🎮 Para ejecutar el juego:"
    echo "   java -jar $SCRIPT_DIR/$JAR_NAME"
    echo ""
    echo "   O haz doble clic en $JAR_NAME"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
else
    echo "❌ Error al crear el JAR"
    exit 1
fi
