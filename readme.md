# E-Commerce App Backend Demo for OOP2

A Java backend demo for an e-commerce workflow, focused on OOP design and modern Java features (including Java 25 preview APIs).

## Tech Stack
- Language: Java 25 (Preview enabled)
- Runtime: Temurin JDK 25.0.2 (recommended)
- IDE: VSCode (WSL) / IntelliJ IDEA / Eclipse

## Install Requirements
- Git
- JDK 25 installed and available
- Preview features enabled at compile and run time (`--enable-preview`)
- Optional (recommended for WSL/Linux): SDKMAN

### Optional SDKMAN setup
```bash
sdk install java 25.0.2-tem
sdk env init
sdk env
```

## Build and Run
From project root:

```bash
find src -name '*.java' -print0 | xargs -0 javac --release 25 --enable-preview -d bin
mkdir -p bin/resources
cp src/resources/* bin/resources/
java --enable-preview -cp bin dev.ecommerce.app.ECommerceApp
```

## Key Features
- OOP core: inheritance, polymorphism, encapsulation
- Domain model: `Food`, `Cookware`, `Tableware`
- Records and sealed interfaces: `CreditCard`, `PayPal`, `Payment`
- Pattern matching `switch` for payment dispatch
- Stream API usage: grouping, partitioning, validation, batching (Gatherers)
- Concurrency: `StructuredTaskScope` + `ScopedValue`
- Localization: `ResourceBundle` with default and Chinese bundles

## Localization
- Default bundle: `src/resources/messages.properties`
- Chinese bundle: `src/resources/messages_zh_CN.properties`
- Runtime input data: `src/resources/products.csv`

`ProductManager` loads bundle base name `resources.messages`, then selects locale-specific files automatically.

## Project Structure
```text
src/
├── dev/ecommerce/
│   ├── app/          # App entry and orchestration
│   ├── concurrency/  # Checkout task and scoped context
│   ├── contracts/    # Shared contracts
│   ├── model/        # Product hierarchy
│   ├── payment/      # Sealed payment types
│   └── service/      # ProductManager (I/O, streams, i18n)
└── resources/
    ├── messages.properties
    ├── messages_zh_CN.properties
    └── products.csv
```

## IDE Notes
- VSCode: use workspace tasks/launch config (build before run).
- IntelliJ/Eclipse: set project JDK to 25 and enable preview for both compiler and runtime.