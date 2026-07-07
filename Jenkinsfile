pipeline {
  agent any
  tools {
    maven 'MAVEN_3_9_15'
    jdk 'JDK_21'
  }
  environment {
  //nombre de la imagen que vamos a crear para nuestra aplicación
    IMAGE_NAME = "plantsync-platform"
    TAG = "${env.BUILD_NUMBER}"
  }
  stages {
    stage ('Compile Project') {
      steps {
        withMaven(maven : 'MAVEN_3_9_15') {
            sh 'mvn clean compile'
        }
      }
    }

    /* stage('Validate Checkstyle') {
      steps {
        withMaven(maven: 'MAVEN_3_9_15') {
          sh 'mvn checkstyle:check'
        }
      }
    }
    */

    stage('Validate Unit Tests') {
      steps {
        withMaven(maven: 'MAVEN_3_9_15') {
          sh 'mvn test'
        }
      }
    }

    stage('Validate Test Coverage') {
      steps {
        withMaven(maven: 'MAVEN_3_9_15') {
          sh 'mvn clean verify jacoco:report'
          sh 'mvn jacoco:check'
        }
      }
    }

     stage('SonarQube Analysis') {
         steps {

             // 1. Enviar el código a SonarQube
             withSonarQubeEnv('MiSonarServer') {
                 sh '''
                     mvn clean verify sonar:sonar \
                     -Dsonar.projectKey=plantsync_backend
                 '''
             }

             // 2. Esperar el resultado del Quality Gate
             script {
                 timeout(time: 10, unit: 'MINUTES') {

                     def qg = waitForQualityGate()

                     // 3. Validar el resultado
                     if (qg.status != 'OK') {
                         error "El pipeline se detuvo porque el proyecto no superó el Quality Gate. Estado: ${qg.status}"
                     }
                 }
             }
         }
     }

    stage('Construir Imagen Docker') {
        steps {
            script {

                echo "Iniciando la construcción de la imagen Docker: ${IMAGE_NAME}:${TAG}"

                // Ejecuta la construcción de la imagen Docker utilizando Docker Buildx
                // Se genera una imagen compatible con arquitectura AMD64,
                // ampliamente utilizada en servidores de producción y entornos cloud.

                echo "Construyendo imagen híbrida/compatible con servidores de producción (AMD64)..."

                // Genera la imagen versionada utilizando el número de ejecución de Jenkins
                sh "docker buildx build --platform linux/amd64 -t ${IMAGE_NAME}:${TAG} --load ."

                // Genera adicionalmente la etiqueta 'latest' para representar la última versión estable
                sh "docker buildx build --platform linux/amd64 -t ${IMAGE_NAME}:latest --load ."

                echo "Imagen construida exitosamente."

            }
        }
    }
    /*stage ('Package Project') {
        steps {
            withMaven(maven : 'MAVEN_3_9_15') {
                sh 'mvn package'
            }
        }
    }*/
  }
}